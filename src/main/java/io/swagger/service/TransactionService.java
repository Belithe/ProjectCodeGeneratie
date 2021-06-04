package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    private OffsetDateTime localTime;
    private static final String bankIban = "NL01INHO0000000001";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static final Pattern VALID_IBAN_REGEX =
            Pattern.compile("^NL\\d{2}INHO\\d{10}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateIBAN(String iban) {
        Matcher matcher = VALID_IBAN_REGEX.matcher(iban);
        return matcher.find();
    }

    public static final Pattern VALID_MONEY_REGEX =
            Pattern.compile("^[+-]?[0-9]{1,3}(?:[0-9]*(?:[.,][0-9]{2})?|(?:,[0-9]{3})*(?:\\.[0-9]{2})?|(?:\\.[0-9]{3})*(?:,[0-9]{2})?)$");

    public static boolean validateAmount(String amount) {
        Matcher matcher = VALID_MONEY_REGEX.matcher(amount);
        return matcher.find();
    }

    // get all transaction
    public List<Transaction> getAllTransactions(Integer limit, Integer offset, String email) throws Exception {
        User user = findUserByEmail(email);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not been found, please try to login again.");
        List<Account> accounts = findAccountById(user);
        List<Transaction> transactions = new ArrayList<>();
        for (UserRole userRole : user.getRole()) {
            switch (userRole) {
                case EMPLOYEE:
                    for (Account account : accounts) {
                        transactions.addAll(transactionRepository.findAll());
                    }
                    break;
                case CUSTOMER:
                    for (Account account : accounts) {
                        transactions.addAll(transactionRepository.findTransactionsByTransferToOrTransferFromOrderByTimestampDesc(account.getIBAN(), account.getIBAN()));
                    }
                    break;
            }
        }
        if (transactions.size() == 0) {
            return transactions;
        }
        return createPage(limit, offset, transactions);
    }

    // get transactions by iban
    public List<Transaction> getTransActionsByIBAN(Integer limit, Integer offset, String email, String iban) throws Exception {
        if (validateIBAN(iban) || iban.isEmpty() || iban == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The IBAN number is incorrect.");
        Account account = accountRepository.findAccountByIBAN(iban);
        if (account == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no know account with this number.");
        if (validateEmail(email) || email.isEmpty() || email == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email number is incorrect.");
        User user = findUserByEmail(email);
        List<Transaction> transactions = new ArrayList<>();
        for (UserRole userRole : user.getRole().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
            if(userRole == UserRole.EMPLOYEE) {
                transactions = transactionRepository.findTransactionsByTransferToOrTransferFromOrderByTimestampDesc(iban, iban);
                break;
            }
            if (userRole == UserRole.CUSTOMER && account.getIBAN() == iban) {
                transactions = transactionRepository.findTransactionsByTransferToOrTransferFromOrderByTimestampDesc(iban, iban);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to this account.");
            }
        }
        return createPage(limit, offset, transactions);
    }

    // make a transaction, deposit or withdraw
    public Transaction checkTypeOfTransaction(String email, PostTransBody postTransBody) throws Exception {
        Transaction transaction = makeObject(postTransBody);
        User user = findUserByEmail(email);
        List<Account> accounts = findAccountById(user);
        switch (transaction.getType()) {
            case TRANSFER:
                // determine the transfer from account
                for (Account account : accounts) {
                    if (transaction.getTransferFrom() == account.getIBAN()) {
                        Account transferToAccount = accountRepository.findAccountByIBAN(transaction.getTransferTo());
                        User transferToUser = userRepository.findById(transferToAccount.getUserId()).get();
                        if (transferToAccount.getAccountType() == AccountType.SAVING && transferToUser.getId() != user.getId())
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer from your saving's account to someone else account.");
                        if (account.getAccountType() == AccountType.SAVING && transferToUser.getId() != user.getId())
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer to an saving's account of someone else.");
                        checkTransactionLimits(transaction, user, account);

                    }
                    break;
                }
                break;
            case DEPOSIT:

                break;
            case WITHDRAW:
                // check if the account got enough to withdraw
                for (Account account : accounts) {
                    if (transaction.getTransferFrom() == account.getIBAN()) {
                        if (account.getBalance() <= 0)
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The balance can not be 0 or lower");

                    }
                }
                break;
        }
        return transactionRepository.save(transaction);
    }

    private void updateBalance() {

    }

    // validate transaction to make
    private void checkTransactionLimits(Transaction transaction, User user, Account account) throws Exception {
        if (transaction == null || user == null || account == null)
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction, user or account is empty");
        // check minimum limit of account
        if ((account.getBalance() - transaction.getAmount()) < account.getMinimumLimit())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The balance would fall below the minimum limit defined by user, change the limit or amount of the transaction.");

        // check amount limit of transaction
        BigDecimal transLimit = new BigDecimal(user.getDayLimit());
        BigDecimal value = new BigDecimal(transaction.getAmount(), new MathContext(3, RoundingMode.HALF_EVEN));
        if (value.compareTo(transLimit) == 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The amount of the transaction overseeded the limit of a transaction defined by the user, change the limit or amount of the transaction.");

        // check day limit of user
        List<Transaction> todaysTrans = getTodayTransactions(transaction);
        float total = 0;
        for (Transaction t : todaysTrans) {
            total += t.getAmount();
        }
        if (total > user.getDayLimit())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The daily limit of the total amount from transactions is overseeded, change the limit or make the transaction on another day.");
    }

    // get today's transaction
    private List<Transaction> getTodayTransactions(Transaction transaction) {
        if (transaction == null)
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "The transaction is empty");
        List<Transaction> transactions = transactionRepository.findTransactionByTransferFrom(transaction.getTransferFrom());
        localTime = OffsetDateTime.now();
        List<Transaction> todayTrans = new ArrayList<>();
        for (Transaction tran : transactions) {
            if (tran.getTimestamp().getYear() == localTime.getYear()
                    && tran.getTimestamp().getMonth() == localTime.getMonth()
                    && tran.getTimestamp().getDayOfMonth() == localTime.getDayOfMonth()) {
               todayTrans.add(tran);
            }
        }
        return todayTrans;
    }

    // make a transaction object
    private Transaction makeObject(PostTransBody postTransBody) {
        if (postTransBody.getTransactionType() == null ||
                postTransBody.getTransferFrom() == null ||
                postTransBody.getTransferTo() == null || postTransBody.getAmount() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the input (type, transfer from, transfer to or amount) is incorrect.");
        localTime = OffsetDateTime.now();
        Transaction transaction = new Transaction();

        if (postTransBody.getTransactionType() == TransactionType.DEPOSIT) {
            transaction.setTransferFrom("ATM");
            transaction.setTransferTo(postTransBody.getTransferTo());
        } else if (postTransBody.getTransactionType() == TransactionType.WITHDRAW) {
            transaction.setTransferFrom(postTransBody.getTransferFrom());
            transaction.setTransferTo("ATM");
        } else {
            transaction.setTransferFrom(postTransBody.getTransferFrom());
            transaction.setTransferTo(postTransBody.getTransferTo());
        }

        transaction.setAmount(postTransBody.getAmount());
        transaction.setType(postTransBody.getTransactionType());

        transaction.setTimestamp(localTime);
        return transaction;
    }

    // find user
    private User findUserByEmail(String email) throws Exception {
        if(userRepository.findByEmailAddress(email) == null)
            return null;
        return userRepository.findByEmailAddress(email);
    }

    // find account by user
    private List<Account> findAccountById(User user) {
        if (!(accountRepository.findAllByUserId(user.getId()).size() <= 0))
            return null;
        return accountRepository.findAllByUserId(user.getId());
    }

    // create page of list transaction
    private List<Transaction>createPage(Integer limit, Integer offset, List<Transaction> transactions) throws Exception {
        if (limit <= 0 || offset < 0)
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit can not be below or eqeal 0, offset can not be below 0");
        if (limit == null && offset == null)
            return transactions;

        int size = transactions.size();

        if (offset == null)
            offset = 0;
        if (limit == null)
            limit = 0;

        limit += offset;
        if (limit > size)
            limit = size;
        if (offset > size)
            offset = size;

        return transactions.subList(offset, limit);
    }
}


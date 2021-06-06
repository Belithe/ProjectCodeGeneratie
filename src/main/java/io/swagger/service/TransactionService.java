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
    private List<UserRole> allUserRoll;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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

    public static final Pattern VALID_INTEGER_REGEX =
            Pattern.compile("^([0-9]+$)?");

    public static boolean validateInteger(String integer) {
        Matcher matcher = VALID_INTEGER_REGEX.matcher(integer);
        return matcher.find();
    }

    // get all transaction
    public List<Transaction> getAllTransactions(Integer offset, Integer limit, String email) throws Exception {
//        if (validateInteger(limit)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The limit is invalid.");
////        if (validateInteger(offset)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The offset is invalid.");
        if (validateEmail(email)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email is invalid.");
        User user = findUserByEmail(email);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not been found, please try to login again.");
        List<Account> accounts = findAccountById(user);
        List<Transaction> transactions = new ArrayList<>();
        allUserRoll = new ArrayList<>();
        allUserRoll.add(UserRole.EMPLOYEE);
        allUserRoll.add(UserRole.CUSTOMER);
        if (user.getRole().containsAll(allUserRoll)) {
            for (Account account : accounts) {
                transactions.addAll(transactionRepository.findByIban(account.getIBAN()));
            }
            List<Transaction> allTran = transactionRepository.findAll();
            allTran.removeAll(transactions);
            transactions.addAll(allTran);
        } else {
            for (UserRole userRole : user.getRole()) {
                switch (userRole) {
                    case EMPLOYEE:
                        transactions = transactionRepository.findAll();
                        break;
                    case CUSTOMER:
                        for (Account account : accounts) {
                            transactions.addAll(transactionRepository.findByIban(account.getIBAN()));
                        }
                        break;
                }
            }
        }
        if (transactions.size() == 0) {
            return transactions;
        }
        return createPage(offset, limit, transactions);
    }

    // get transactions by iban
    public List<Transaction> getTransActionsByIBAN(String email, String iban) throws Exception {
        if (!validateIBAN(iban) || iban.isEmpty() || iban == null)
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
                transactions = transactionRepository.findByIban(iban);
                break;
            }
            if (userRole == UserRole.CUSTOMER && account.getIBAN().equals(iban)) {
                transactions = transactionRepository.findByIban(iban);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to this account.");
            }
        }
        return transactions;
    }

    // make a transaction, deposit or withdraw
    public Transaction createTransaction(String email, PostTransBody postTransBody) throws Exception {
        Transaction transaction = makeObject(postTransBody);
        if (transaction.getTransferFrom() == transaction.getTransferTo())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer from to the same account.");
        User user = findUserByEmail(email);
        List<Account> accounts = findAccountById(user);
        if (accounts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No accounts found. Only customers or employees can make transactions with an account that has a IBAN number.");
//            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Im am a teapot. lol");
        }
        transaction.setUserPerforming(user.getId());
        switch (transaction.getType()) {
            case TRANSFER:
                // determine the transfer from account
                for (Account account : accounts) {
                    if (transaction.getTransferFrom() == account.getIBAN()) {
                        checkTransactionLimits(transaction, user, account);
                        Account transferToAccount = accountRepository.findAccountByIBAN(transaction.getTransferTo());
                        if (transferToAccount != null) {
                            User transferToUser = null;
                            if (userRepository.findById(transferToAccount.getUserId()).isPresent()) {
                                transferToUser = userRepository.findById(transferToAccount.getUserId()).get();
                            }
                            if (transferToUser != null) {
                                if (transferToAccount.getAccountType() == AccountType.SAVING && transferToUser.getId() != user.getId())
                                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer from your saving's account to someone else account.");
                                if (account.getAccountType() == AccountType.SAVING && transferToUser.getId() != user.getId())
                                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer to a saving's account of someone else.");
                            }
                        } else {
                            // The transfer to IBAN is registered on this bank
                        }
                        updateFromBalance(transaction);
                        updateToBalance(transaction);
                    } else {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "There is no account associated with the IBAN number to make ");
                    }
                    break;
                }
                break;

            case DEPOSIT:
                updateToBalance(transaction);
                break;
            case WITHDRAW:
                // check if the account got enough to withdraw
                for (Account account : accounts) {
                    if (transaction.getTransferFrom() == account.getIBAN()) {
                        if (account.getBalance() <= 0)
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The balance can not be 0 or lower");
                        updateFromBalance(transaction);
                    }
                }
                break;
        }
        transactionRepository.save(transaction);
        return transaction;
    }

    // update from account
    private void updateFromBalance(Transaction transaction) {
        Account account = accountRepository.findAccountByIBAN(transaction.getTransferFrom());
        if (account != null) {
            account.setBalance((account.getBalance() - transaction.getAmount()));
            accountRepository.save(account);
        }
    }

    // update to account
    private void updateToBalance(Transaction transaction) {
        Account account = accountRepository.findAccountByIBAN(transaction.getTransferTo());
        if (account != null) {
            account.setBalance((account.getBalance() + transaction.getAmount()));
            accountRepository.save(account);
        }
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
        if (transLimit.compareTo(value) == -1)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the input (type, transfer from, transfer to or amount) is missing.");
        localTime = OffsetDateTime.now();
        Transaction transaction = new Transaction();

        switch (postTransBody.getTransactionType()) {
            case WITHDRAW:
                transaction.setTransferTo("ATM");
                if (!validateIBAN(postTransBody.getTransferFrom()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The IBAN to transfer from is incorrect.");
                transaction.setTransferFrom(postTransBody.getTransferFrom());
                break;
            case DEPOSIT:
                transaction.setTransferFrom("ATM");
                if (!validateIBAN(postTransBody.getTransferTo()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The IBAN to transfer to is incorrect.");
                transaction.setTransferTo(postTransBody.getTransferTo());
                break;
            case TRANSFER:
                if (!validateIBAN(postTransBody.getTransferTo()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The IBAN to transfer to is incorrect.");
                if (!validateIBAN(postTransBody.getTransferTo()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The IBAN to transfer from is incorrect.");
                transaction.setTransferFrom(postTransBody.getTransferFrom());
                transaction.setTransferTo(postTransBody.getTransferTo());
                break;
        }

        transaction.setAmount(postTransBody.getAmount());
        transaction.setType(postTransBody.getTransactionType());

        transaction.setTimestamp(localTime);
        return transaction;
    }

    // find user
    private User findUserByEmail(String email) {
        if(userRepository.findByEmailAddress(email) == null)
            return null;
        return userRepository.findByEmailAddress(email);
    }

    // find account by user
    private List<Account> findAccountById(User user) {
        if (user == null || accountRepository.findAllByUserId(user.getId()).size() <= 0)
            return null;
        return accountRepository.findAllByUserId(user.getId());
    }

    // create page of list transaction
    private List<Transaction>createPage(Integer offset, Integer limit, List<Transaction> transactions) throws Exception {
        if (limit <= 0 || offset < 0)
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit can not be below or eqeal 0, offset can not be below 0");
        if (limit == null && offset == null)
            return transactions;

        if (offset == null)
            offset = 0;
        if (limit == null)
            limit = 0;

        int size = transactions.size();
        limit = limit + offset;
        if (limit > size)
            limit = size;
        if (offset > size)
            offset = size;

        return transactions.subList(offset, limit);
    }
}


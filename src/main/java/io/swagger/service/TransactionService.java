//package io.swagger.service;
//
//import io.swagger.api.NotFoundException;
//import io.swagger.model.*;
//import io.swagger.repository.AccountRepository;
//import io.swagger.repository.TransactionRepository;
//import io.swagger.repository.UserRepository;
//import io.swagger.security.JwtTokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.server.ResponseStatusException;
//import org.threeten.bp.LocalDate;
//import org.threeten.bp.OffsetDateTime;
//import org.threeten.bp.ZoneOffset;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class TransactionService {
//    @Autowired
//    TransactionRepository transactionRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    private OffsetDateTime localTime;
//    private float dayLimit;
//    private Integer transactionLimit;
//
//    // constructor
//    public TransactionService() {
//
//    }
//
//    public void testAddTransaction() {
//        Transaction transaction = new Transaction();
//        transaction.setTransactionId(1);
//        transaction.setUserPerforming(1);
//        transaction.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
//        transaction.setTransferTo("NL01INHO0000000002");
//        transaction.setTransferFrom("NL01INHO0000000003");
//        transaction.setAmount((float) 50);
//        transaction.setType(TransactionType.TRANSFER);
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setTransactionId(2);
//        transaction2.setUserPerforming(1);
//        transaction2.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
//        transaction2.setTransferTo("NL01INHO0000000003");
//        transaction2.setTransferFrom("NL01INHO0000000002");
//        transaction2.setAmount((float) 50);
//        transaction2.setType(TransactionType.TRANSFER);
//        transactionRepository.save(transaction);
//        transactionRepository.save(transaction2);
//    }
//
//    // get all transaction
//    public List<Transaction> getAllTransactions(Integer limit, Integer offset, String email) {
//        User user = findUser(email);
//        List<Account> accounts = findAccount();
//        if (user.getRole() == UserRole.EMPLOYEE) {
//            List<Transaction> transactions = transactionRepository.findAll();
//        }
//        if (transactions.size() == 0) {
//            return transactions;
//        }
//        return createPage(limit, offset, transactions);
//    }
//
//    public List<Transaction> getTransActionsByIBAN(Integer limit, Integer offset, String email, String iban) {
//        User user = findUser(email);
//        List<Account> accounts = findAccount();
//        if () {
//
//        }
//        List<Transaction> transactions = transactionRepository.findTransactionsByTransferToOrTransferFromOrderByTimestampDesc(iban, iban);
//        return createPage(limit, offset, transactions);
//    }
//
//    // make a deposit or withdraw
//    public Transaction incomingChangeBalance(String email, TransactionType type, String transferFrom, String transferTo, Float amount) {
//        Transaction transaction = makeObject(type, transferFrom, transferTo, amount);
//        User user = findUser(email);
//
//
//        if ()
//        switch (type) {
//            case TRANSFER:
//
//                break;
//            case DEPOSIT:
//
//                break;
//            case WITHDRAW:
//
//                break;
//        }
//        return transactionRepository.save(transaction);
//    }
//
//    // validate input
//    private void transactionLimit(Transaction transaction, User user) throws Exception {
//        if ()
//            throw new Exception();
//    }
//
//    // make a transaction object
//    private Transaction makeObject(TransactionType type, String transferFrom, String transferTo, Float amount) {
//        Transaction transaction = new Transaction();
//        if (type == TransactionType.DEPOSIT) {
//            transaction.setTransferFrom("ATM");
//            transaction.setTransferTo(transferTo);
//        } else if (type == TransactionType.WITHDRAW) {
//            transaction.setTransferFrom(transferFrom);
//            transaction.setTransferTo("ATM");
//        } else {
//            transaction.setTransferFrom(transferFrom);
//            transaction.setTransferTo(transferTo);
//        }
//        transaction.setTransferFrom(transferFrom);
//        transaction.setAmount(amount);
//        transaction.setType(type);
//        transaction.setTimestamp(localTime.now());
//        return transaction;
//    }
//
//    // find user
//    private User findUser(String email) {
//        User user = new User();
//        return user = userRepository.findByEmailAddress(email);
//    }
//
//    // find account by user
//    private List<Account> findAccount(User user) {
//        List<Account> accounts = accountRepository.findAllById(Collections.singleton(new Long(user.getId())));
//        return accounts;
//    }
//
//    // create page of list transaction
//    private List<Transaction>createPage(Integer limit, Integer offset, List<Transaction>transactions) {
//        if (limit == null && offset == null)
//            return transactions;
//
//        int size = transactions.size();
//
//        if (offset == null)
//            offset = 0;
//        if (limit == null)
//            limit = 0;
//        transactions.subList(offset, limit);
//
//        return transactions;
//    }
//}
//

package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    private OffsetDateTime localTime;
    private User loggedInUser;
    private float dayLimit;
    private Integer transactionLimit;

    // constructor
    public TransactionService() {

    }

    // get all transaction
    public List<Transaction> getAllTransactions(Integer limit, Integer offset, String email) {
        findUser(email);
        List<Account> accounts = findAccount();
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.size() == 0) {
            return transactions;
        }
        return createPage(limit, offset, transactions);
    }

    // update balance
    public void updateBalance(Transaction transaction) {

    }

    // make a deposit or withdraw
    public Transaction makeTransaction(TransactionType type, String transferFrom, String transferTo, Float amount) {
        Transaction transaction = makeObject(type, transferFrom, transferTo, amount);
        switch (type) {
            case TRANSFER:
                makeTransaction(transaction);
                break;
            case DEPOSIT:
                makeDeposit(transaction);
                break;
            case WITHDRAW:
                 makeWithdraw(transaction);
                break;
        }
        return transactionRepository.save(transaction);
    }

    public Transaction makeObject(TransactionType type, String transferFrom, String transferTo, Float amount) {
        Transaction transaction = new Transaction();
        transaction.setTransferTo(transferTo);
        transaction.setTransferFrom(transferFrom);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(localTime.now());
        transaction.setType(type);
        return transaction;
    }

    public void makeTransaction(Transaction transaction) {

    }

    // make deposit
    public void makeDeposit(Transaction transaction) {

    }

    // make withdraw
    public void makeWithdraw(Transaction transaction) {

    }

    // find user
    private void findUser(String email) {
        loggedInUser = new User();
        loggedInUser = userRepository.findByEmailAddress(email);
    }

    // find account by user
    private List<Account> findAccount() {
        List<Account> accounts = accountRepository.findAllById(Collections.singleton(new Long(loggedInUser.getId())));
        return accounts;
    }

    // create page of list transaction
    private List<Transaction>createPage(Integer limit, Integer offset, List<Transaction>transactions) {
        if (limit == null && offset == null)
            return transactions;

        int size = transactions.size();

        if (offset == null)
            offset = 0;
        if (limit == null)
            limit = 0;
        transactions.subList(offset, limit);

        return transactions;
    }
}


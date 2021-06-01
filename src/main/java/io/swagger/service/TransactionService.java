package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.Account;
import io.swagger.model.Body1;
import io.swagger.model.Transaction;
import io.swagger.model.User;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    AccountRepository accountRepository;
    private LocalDateTime localTime;
    private User loggedInUser;

    // get all transaction
    public List<Transaction> getAllTransactions(Integer limit, Integer offset) {
        List<Transaction> transactions = transactionRepository.findTransactionsByTransferToOrTransferFromOrderByTimestampDesc();

        return ;
    }




    public void updateBalance(Transaction transaction) {

    }

    // make a deposit or withdraw
    public Transaction makeDepositOrWithdraw() {
        Transaction transaction = new Transaction();
        return transactionRepository.save(transaction);
    }

    // make deposit
    public void makeDeposit() {

    }

    // make withdraw
    public void makeWithdraw() {

    }

    // find user
    private User findUser() {
        User user = new User();
        return user;
    }

    // find account by user
    private Account findAccount() {
        Account account = new Account();
        return account;
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


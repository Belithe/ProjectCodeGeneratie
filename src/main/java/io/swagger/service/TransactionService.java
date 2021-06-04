package io.swagger.service;

import io.swagger.model.Transaction;
import io.swagger.repository.TransactionRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    public Transaction add(Transaction user) {

        return transactionRepository.save(user);
    }



    public List<Transaction> getAllUsers() {
        return (List<Transaction>) transactionRepository.findAll();
    }

//    public Transaction getUserById(int id) { return TransactionRepository.findById(id); }


}


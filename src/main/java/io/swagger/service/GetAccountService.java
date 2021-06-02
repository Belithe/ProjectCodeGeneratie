package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.Account;
import io.swagger.model.Body1;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GetAccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Account getByIBAN(String IBANToGet) {
        return accountRepository.findAccountByIBAN(IBANToGet);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

}


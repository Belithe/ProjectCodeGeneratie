package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.Body3;
import io.swagger.model.Account;
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
public class AccountManagementService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    GetAccountService getAccountService;

    public void updateExistingAccount(String IBAN, Body3 body) throws NotFoundException {

        Account accountToEdit = getAccountService.getByIBAN(IBAN);

        if (accountToEdit == null) {
            throw new NotFoundException(404, "Could not find an account with the given IBAN.");
        }

        if (body.getMinimumLimit() != null) {
            accountToEdit.setMinimumLimit(body.getMinimumLimit());
        } else {
            throw new NotFoundException(400, "The given input was not valid for this endpoint.");
        }

        accountRepository.save(accountToEdit);
    }

    public void deleteSingleAccount(String IBAN) throws NotFoundException {
        Account accountToDelete = getAccountService.getByIBAN(IBAN);

        if (accountToDelete == null) {
            throw new NotFoundException(404, "Could not find an user with the given user ID.");
        }

        accountRepository.delete(accountToDelete);

    }



}


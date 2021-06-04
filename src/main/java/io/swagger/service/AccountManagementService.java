package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.CreateAccountPostBody;
import io.swagger.model.Account;
import io.swagger.model.UpdateAccountPutBody;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

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

    public Account getByIBAN(String IBANToGet) {
        return accountRepository.findAccountByIBAN(IBANToGet);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAllAccountsById(Integer userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public void updateExistingAccount(String IBAN, UpdateAccountPutBody body) throws ResponseStatusException {

        Account accountToEdit = this.getByIBAN(IBAN);

        if (accountToEdit == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (body.getMinimumLimit() != null) {
            accountToEdit.setMinimumLimit(body.getMinimumLimit());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        accountRepository.save(accountToEdit);
    }

    public void deleteSingleAccount(String IBAN) throws NotFoundException {
        Account accountToDelete = this.getByIBAN(IBAN);

        if (accountToDelete == null) {
            throw new NotFoundException(404, "Could not find a user with the given user ID.");
        }

        accountRepository.delete(accountToDelete);

    }

    public void createNewAccount(CreateAccountPostBody accountToCreate) throws ResponseStatusException {
        if(!Pattern.matches("^NL\\d{2}INHO\\d{10}", "hi lol")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            Account accountToAdd = new Account();

            accountToAdd.setUserId(accountToCreate.getUserId());
            accountToAdd.setIBAN(accountToCreate.getIBAN());
            accountToAdd.setMinimumLimit(accountToCreate.getMinimumLimit());
            accountToAdd.setBalance(0F);
            accountToAdd.setAccountType(accountToCreate.getAccountType());

            accountRepository.save(accountToAdd);
        }


    }

}


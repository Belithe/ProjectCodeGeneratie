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
        if(accountRepository.findAccountByIBAN(IBANToGet) != null) {
            return accountRepository.findAccountByIBAN(IBANToGet);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAllAccountsById(Integer userId) throws ResponseStatusException {
        if(!accountRepository.findAllByUserId(userId).isEmpty()) {
            return accountRepository.findAllByUserId(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void updateExistingAccount(String IBAN, UpdateAccountPutBody body) throws ResponseStatusException {

        Account accountToEdit = this.getByIBAN(IBAN);

        if (accountToEdit == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (body.getMinimumLimit() != null) {
            accountToEdit.setMinimumLimit(body.getMinimumLimit());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No minimum limit was supplied");
        }

        accountRepository.save(accountToEdit);
    }

    public void deleteSingleAccount(String IBAN) throws ResponseStatusException {
        Account accountToDelete = this.getByIBAN(IBAN);

        if (accountToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        accountRepository.delete(accountToDelete);

    }

    public void createNewAccount(CreateAccountPostBody accountToCreate) throws ResponseStatusException {
        if (accountToCreate.getUserId() == null || accountToCreate.getIBAN() == null || accountToCreate.getMinimumLimit() == null || accountToCreate.getAccountType() == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more fields was not given");
        } else if (accountRepository.findAccountByIBAN(accountToCreate.getIBAN()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account with supplied IBAN was already found");
        } else if(!Pattern.matches("^NL\\d{2}INHO\\d{10}$", accountToCreate.getIBAN())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IBAN does not match official pattern");
        } else {
            Account accountToAdd = new Account();

            accountToAdd.setUserId(accountToCreate.getUserId());
            accountToAdd.setIBAN(accountToCreate.getIBAN());
            accountToAdd.setMinimumLimit(accountToCreate.getMinimumLimit());
            accountToAdd.setAccountType(accountToCreate.getAccountType());

            accountToAdd.setBalance(0F);
            accountRepository.save(accountToAdd);
        }
    }
}


package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest (classes = {Swagger2SpringBoot.class})
@AutoConfigureMockMvc
class AccountManagementServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountManagementService accountManagementService;

    @MockBean
    AccountRepository accountRepository;

    List<Account> expectedAccounts;

    @BeforeEach
    public void setup(){
        List<Account> accounts = new ArrayList<>();

        // Account 1 is a saving account
        Account account1 = new Account();
        account1.setMinimumLimit(0f);
        account1.setBalance(400f);
        account1.setAccountType(AccountType.SAVING);
        account1.setIBAN("NL19INHO6296399613");
        account1.setUserId(1);

        accounts.add(account1);

        // Account 2 is a current account
        Account account2 = new Account();
        account2.setMinimumLimit(0f);
        account2.setBalance(510f);
        account2.setAccountType(AccountType.CURRENT);
        account2.setIBAN("NL19INHO1259637692");
        account2.setUserId(2);

        accounts.add(account2);

        // Account 3 is a saving account
        Account account3 = new Account();
        account3.setMinimumLimit(0f);
        account3.setBalance(640f);
        account3.setAccountType(AccountType.SAVING);
        account3.setIBAN("NL19INHO3286319395");
        account3.setUserId(3);

        accounts.add(account3);

        expectedAccounts = accounts;
    }

    @Test
    public void getAllAccounts() {
        // Setup
        given(accountRepository.findAll()).willReturn(expectedAccounts);

        // Execution
        List<Account> accounts = accountRepository.getAllAccounts();

        // Assertions
        assertNotNull(accounts);
        assertEquals(3, accounts.size());
        assertEquals(accounts, expectedAccounts);
    }

    @Test
    public void getAccountByIban() {
        // Setup
        given(accountRepository.findById("1")).willReturn(Optional.of(expectedAccounts.get(0)));

        // Execution
        Account account = accountManagementService.getByIBAN("NL19INHO6296399613");

        // Assertions
        assertNotNull(account);
        assertEquals(1, account.getUserId());
        assertEquals(expectedAccounts.get(0), account);
    }

    @Test
    public void getNonExistingAccountByIban() {
        // Setup
        given(accountRepository.findById("1")).willReturn(Optional.empty());

        // Execution
        Account account = accountManagementService.getByIBAN("NL19INHO6296399613");

        // Assertions
        assertNull(account);
    }

    @Test
    public void updateAccount

}
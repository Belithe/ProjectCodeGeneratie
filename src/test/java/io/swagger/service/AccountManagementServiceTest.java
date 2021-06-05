package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.CreateAccountPostBody;
import io.swagger.model.UpdateAccountPutBody;
import io.swagger.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

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

        // Account 4 is a current account
        Account account4 = new Account();
        account4.setBalance(1000f);
        account4.setAccountType(AccountType.CURRENT);
        account4.setIBAN("NL01INHO0000000002");
        account4.setMinimumLimit(50f);
        account4.setUserId(4);
        accountRepository.save(account4);

        accounts.add(account4);

        // Account 5 is a saving account
        Account account5 = new Account();
        account5.setBalance(1000f);
        account5.setAccountType(AccountType.SAVING);
        account5.setIBAN("NL01INHO0000000004");
        account5.setMinimumLimit(50f);
        account5.setUserId(4);
        accountRepository.save(account5);

        accounts.add(account5);


        expectedAccounts = accounts;
    }

    // Get tests
    @Test
    public void getAllAccounts() {
        // Setup
        given(accountRepository.findAll()).willReturn(expectedAccounts);

        // Execution
        List<Account> accounts = accountManagementService.getAllAccounts();

        // Assertions
        assertNotNull(accounts);
        assertEquals(5, accounts.size());
        assertEquals(accounts, expectedAccounts);
    }

    @Test
    public void getAccountByIban() {
        // Setup
        given(accountRepository.findAccountByIBAN("NL19INHO6296399613")).willReturn(expectedAccounts.get(0));

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
        given(accountRepository.findAccountByIBAN("0")).willReturn(null);

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.getByIBAN("0");
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAccountsByUserId() {
        // Setup
        List<Account> user1Accounts = new ArrayList<>();
        user1Accounts.add(expectedAccounts.get(3));
        user1Accounts.add(expectedAccounts.get(4));

        given(accountRepository.findAllByUserId(4)).willReturn(user1Accounts);

        // Execution
        List<Account> accounts = accountManagementService.getAllAccountsById(4);

        // Assertions
        assertNotNull(accounts);
        assertEquals(accounts, user1Accounts);

    }

    @Test
    public void getAccountsByFakeUserId() {
        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.getAllAccountsById(99);
        });
        // Assertions
        assertTrue(exception.getStatus() == HttpStatus.NOT_FOUND);
    }

    // Put tests
    @Test
    public void updateAccountSuccessfully() {
        // Setup
        given(accountRepository.findAccountByIBAN("NL19INHO629399613")).willReturn(expectedAccounts.get(0));

        // Execution
        accountManagementService.updateExistingAccount("NL19INHO629399613", new UpdateAccountPutBody().minimumLimit(200f));

        // Assertions
        // None :)
    }

    @Test
    public void updateAccountNonExistent() {
        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                accountManagementService.updateExistingAccount("0", new UpdateAccountPutBody().minimumLimit(200f));
                });
        // Assertions
        assertTrue(exception.getStatus() == HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateAccountMinimumLimitAbsent() {
        // Setup
        given(accountRepository.findAccountByIBAN("NL19INHO629399613")).willReturn(expectedAccounts.get(0));

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.updateExistingAccount("NL19INHO629399613", new UpdateAccountPutBody().minimumLimit(null));
        });

        // Assertions

        assertEquals(exception.getStatus(), HttpStatus.NO_CONTENT);
        assertTrue(exception.getMessage().contains("No minimum limit was supplied"));

    }

    // Create tests

    public CreateAccountPostBody getBaseCreateAccountBody() {
        CreateAccountPostBody baseBody = new CreateAccountPostBody();

        baseBody.setAccountType(AccountType.CURRENT);
        baseBody.setIBAN("NL19INHO6296399613");
        baseBody.setMinimumLimit(200f);
        baseBody.setUserId(4);

        return baseBody;
    }

    @Test
    public void createAccountSuccessfully() {
        // Execution
        accountManagementService.createNewAccount(getBaseCreateAccountBody());

    }

    @Test
    public void createAccountExistingIBAN() {
        //Setup
        given(accountRepository.findAccountByIBAN("NL19INHO6296399613")).willReturn(expectedAccounts.get(0));

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.createNewAccount(getBaseCreateAccountBody());
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getMessage().contains("Account with supplied IBAN was already found"));

    }

    @Test
    public void createAccountLackingType() {
        //Setup
        CreateAccountPostBody badAccount = getBaseCreateAccountBody();
        badAccount.setAccountType(null);

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.createNewAccount(badAccount);
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getMessage().contains("One or more fields was not given"));
    }

    @Test
    public void createAccountLackingLimit() {
        //Setup
        CreateAccountPostBody badAccount = getBaseCreateAccountBody();
        badAccount.setMinimumLimit(null);

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.createNewAccount(badAccount);
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getMessage().contains("One or more fields was not given"));
    }

    @Test
    public void createAccountLackingIBAN() {
        //Setup
        CreateAccountPostBody badAccount = getBaseCreateAccountBody();
        badAccount.setIBAN(null);

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.createNewAccount(badAccount);
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getMessage().contains("One or more fields was not given"));
    }

    @Test
    public void createAccountLackingUserId() {
        //Setup
        CreateAccountPostBody badAccount = getBaseCreateAccountBody();
        badAccount.setUserId(null);

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.createNewAccount(badAccount);
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getMessage().contains("One or more fields was not given"));
    }

    @Test
    public void createAccountIllegalIBAN() {
        //Setup
        CreateAccountPostBody badAccount = getBaseCreateAccountBody();
        badAccount.setIBAN("ILLEGAL");

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.createNewAccount(badAccount);
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getMessage().contains("IBAN does not match official pattern"));

    }

    // Delete tests

    @Test
    public void deleteAccountSuccessfully() {

        // Setup
        given(accountRepository.findAccountByIBAN("NL19INHO6296399613")).willReturn(expectedAccounts.get(0));

        // Execution
        accountManagementService.deleteSingleAccount("NL19INHO6296399613");
    }

    @Test
    public void deleteAccountNotExisting() {

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountManagementService.deleteSingleAccount("NL19INHO6296399612");
        });

        // Assertions
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }
}
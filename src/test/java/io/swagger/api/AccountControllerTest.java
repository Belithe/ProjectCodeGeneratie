package io.swagger.api;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountManagementService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("unitTesting")
@SpringBootTest(classes = { Swagger2SpringBoot.class })
@AutoConfigureMockMvc
public class AccountControllerTest {
    @MockBean
    AccountManagementService accountManagementService;

    @MockBean
    UserService userService;

    @MockBean
    AccountRepository accountRepository;

    @Autowired
    AccountsApiController accountsApiController;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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

        User alice = new User();
        alice.id(1);
        alice.firstName("Alice");
        alice.lastName("Alixon");
        alice.emailAddress("alice@example.com");
        alice.addRoleItem(UserRole.EMPLOYEE);
        alice.phone("+31 6 12345678");
        alice.transactionLimit(BigDecimal.valueOf(100f));
        alice.dayLimit(1000f);
        alice.birthDate(LocalDate.of(2010, 10, 10));
        alice.password("idk");

        User bob = new User();
        bob.id(2);
        bob.firstName("Bob");
        bob.lastName("Bobbery");
        bob.emailAddress("bob@example.com");
        bob.addRoleItem(UserRole.CUSTOMER);
        bob.phone("+31 6 12345678");
        bob.transactionLimit(BigDecimal.valueOf(200f));
        bob.dayLimit(2000f);
        bob.birthDate(LocalDate.of(2000, 10, 10));
        bob.password("idk");

        expectedAccounts = accounts;

        given(userService.getUserByEmailAddress("alice@example.com")).willReturn(alice);
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(bob);
    }

    // Successful get all
    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllAccounts() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Execution
        ResponseEntity<List<Account>> AccountsResponse = accountsApiController.getAccounts(20, 1, null);

        // Assertions
        assertEquals(5, AccountsResponse.getBody().size());
        assertEquals(expectedAccounts, AccountsResponse.getBody());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllAccountsLimited() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Execution
        ResponseEntity<List<Account>> AccountsResponse = accountsApiController.getAccounts(2, 1, null);

        // Assertions
        assertNotNull(AccountsResponse);
        assertNotNull(AccountsResponse.getBody());
        assertEquals(2, AccountsResponse.getBody().size());

        // Need to compare each item as the lists are not the same instance
        List<Account> expectedLimitedAccounts = expectedAccounts.subList(0, 1);
        for (int i = 0; i < expectedLimitedAccounts.size(); i++) {
            Account expectedAccount = expectedLimitedAccounts.get(i);
            Account respondedAccount = AccountsResponse.getBody().get(i);

            assertEquals(expectedAccount, respondedAccount);
        }
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllAccountsLimitedSecondPage() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Execution
        ResponseEntity<List<Account>> AccountsResponse = accountsApiController.getAccounts(2, 2, null);

        // Assertions
        assertNotNull(AccountsResponse);
        assertNotNull(AccountsResponse.getBody());
        assertEquals(2, AccountsResponse.getBody().size());

        // Need to compare each item as the lists are not the same instance
        List<Account> expectedLimitedAccounts = expectedAccounts.subList(2, 3);
        for (int i = 0; i < expectedLimitedAccounts.size(); i++) {
            Account expectedAccount = expectedLimitedAccounts.get(i);
            Account respondedAccount = AccountsResponse.getBody().get(i);

            assertEquals(expectedAccount, respondedAccount);
        }
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllAccountsLimitedFourthPage() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Execution
        ResponseEntity<List<Account>> AccountsResponse = accountsApiController.getAccounts(2, 4, null);

        // Assertions
        assertNotNull(AccountsResponse);
        assertNotNull(AccountsResponse.getBody());
        assertEquals(0, AccountsResponse.getBody().size());
    }



    // Successful get by id
    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAccountsByOthersIdAsEmployee() {
        // Setup
        given(accountManagementService.getAllAccountsById(4)).willReturn(expectedAccounts.subList(3,5));

        // Execution
        ResponseEntity<List<Account>> gottenAccounts = accountsApiController.getAccounts(20, 1, 4);

        // Assertions
        assertEquals(2, gottenAccounts.getBody().size());
        assertEquals(expectedAccounts.subList(3,5), gottenAccounts.getBody());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAccountsByOwnIdAsEmployee() {
        // Setup
        given(accountManagementService.getAllAccountsById(1)).willReturn(expectedAccounts.subList(0,1));

        // Execution
        ResponseEntity<List<Account>> gottenAccounts = accountsApiController.getAccounts(20, 1, 1);

        // Assertions
        assertEquals(1, gottenAccounts.getBody().size());
        assertEquals(expectedAccounts.subList(0,1), gottenAccounts.getBody());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAccountsByOwnIdAsCustomer() {
        // Setup
        given(accountManagementService.getAllAccountsById(2)).willReturn(expectedAccounts.subList(1,2));

        // Execution
        ResponseEntity<List<Account>> gottenAccounts = accountsApiController.getAccounts(20, 1, 2);

        // Assertions
        assertEquals(1, gottenAccounts.getBody().size());
        assertEquals(expectedAccounts.subList(1,2), gottenAccounts.getBody());
    }


    // Failed get all
    @Test
    public void getAllAccountsWithoutAuthentication() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.getAccounts(20, 1, null);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAllAccountsWithoutProperAuthorization() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.getAccounts(20, 1, null);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    // Failed get by id
    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAccountsByOthersIdAsCustomer() {
        // Setup
        given(accountManagementService.getAllAccountsById(4)).willReturn(expectedAccounts.subList(3,5));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.getAccounts(20, 1, 4);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    // Get by IBAN
    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAccountByIBANAsEmployee() {
        // Setup
        given(accountManagementService.getByIBAN("NL19INHO6296399613")).willReturn(expectedAccounts.get(0));

        // Executions
        ResponseEntity<Account> retrievedAccount = accountsApiController.getAccountsByIBAN("NL19INHO6296399613");

        // Assertions
        assertEquals(expectedAccounts.get(0),retrievedAccount.getBody());

    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAccountByOwnedIBANAsCustomer() {
        // Setup
        given(accountManagementService.getByIBAN("NL19INHO1259637692")).willReturn(expectedAccounts.get(1));

        // Executions
        ResponseEntity<Account> retrievedAccount = accountsApiController.getAccountsByIBAN("NL19INHO1259637692");

        // Assertions
        assertEquals(expectedAccounts.get(1),retrievedAccount.getBody());

    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAccountByOtherIBANAsCustomer() {
        // Setup
        given(accountManagementService.getByIBAN("NL19INHO6296399613")).willReturn(expectedAccounts.get(0));

        // Executions

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.getAccountsByIBAN("NL19INHO6296399613");
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    // Create test
    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void createAccountAsEmployee() {
        CreateAccountPostBody testBody = new CreateAccountPostBody();
        testBody.setUserId(2);
        testBody.setAccountType(AccountType.CURRENT);
        testBody.setIBAN("NL19INHO3286319395");
        testBody.setMinimumLimit(200f);

        // Execution
        ResponseEntity<Account> responseEntity = accountsApiController.createAccount(testBody);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void createAccountAsCustomer() {
        CreateAccountPostBody testBody = new CreateAccountPostBody();
        testBody.setUserId(1);
        testBody.setAccountType(AccountType.CURRENT);
        testBody.setIBAN("NL19INHO3286319395");
        testBody.setMinimumLimit(200f);

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.createAccount(testBody);
        });
        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


    // Delete test
    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void deleteAccountAsEmployee() {
        // Execution
        ResponseEntity<Void> responseEntity = accountsApiController.deleteAccount("NL19INHO3286319395");

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void deleteAccountAsCustomer() {
        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.deleteAccount("NL19INHO3286319395");
        });
        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }


}

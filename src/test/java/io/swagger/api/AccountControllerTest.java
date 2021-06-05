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
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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

        expectedAccounts = accounts;

        given(userService.getUserByEmailAddress("alice@example.com")).willReturn(alice);
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
    public void getAccountsByOthersId() {
        // Setup
        given(accountManagementService.getAllAccountsById(4)).willReturn(expectedAccounts.subList(3,4));
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
    @WithMockUser(username = "alice@example.com", authorities = { "CUSTOMER" })
    public void getAllAccountsWithoutProperAuthorization() {
        // Setup
        given(accountManagementService.getAllAccounts()).willReturn(expectedAccounts);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            accountsApiController.getAccounts(20, 1, null);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }
}

package io.swagger.api;

import io.swagger.Swagger2SpringBoot;
import io.swagger.api.TransactionsApiController;
import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("unitTesting")
@SpringBootTest(classes = { Swagger2SpringBoot.class })
@AutoConfigureMockMvc
public class TransactionControllerTest {

    // test users
    @MockBean
    UserRepository userRepository;

    List<User> expectedUsers;

    // test accounts
    @MockBean
    AccountRepository accountRepository;

    List<Account> expectedAccounts;
    List<Account> expectedAccountsPerUser;
    List<Account> expectedAccountsPerCustomer;
    List<Account> expectedAccountsPerCustomerEmplyee;
    Account expectedAccountPerIban;

    // test transaction

    @Autowired
    TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;

    @Autowired
    TransactionsApiController transactionsApiController;

    List<Transaction> expectedTransactions;
    List<Transaction> expectedTransactionsPerUser;
    List<Transaction> expectedTransactionsByIBAN;
    List<Transaction> expectedTransactionSaveCustomer;
    List<Transaction> expectedTransactionCurrentCustomer;
    List<Transaction> expectedTransactionByCustomer;

    List<PostTransBody> expectedPostTran;

    @BeforeEach
    public void setup() {
        List<User> users = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        List<Account> accountsPerUser = new ArrayList<>();
        List<Account> accountsPerCustomer = new ArrayList<>();
        List<Account> accountsPerCustomerEmplyee = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        List<Transaction> transactionsPerUser = new ArrayList<>();
        List<Transaction> transactionsByIBAN = new ArrayList<>();
        List<Transaction> transactionsByCustomerSaving = new ArrayList<>();
        List<Transaction> transactionsByCustomerCurrent = new ArrayList<>();
        List<PostTransBody> posts = new ArrayList<>();
        // Users

        // Alice is just an employee
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

        users.add(alice);

        // bob is just a customer
        User bob = new User();
        bob.id(2);
        bob.firstName("Bob");
        bob.lastName("Bobson");
        bob.emailAddress("bob@example.com");
        bob.addRoleItem(UserRole.CUSTOMER);
        bob.phone("+31 6 87654321");
        bob.transactionLimit(BigDecimal.valueOf(20f));
        bob.dayLimit(30f);
        bob.birthDate(LocalDate.of(2012, 12, 12));
        bob.password("idk");

        users.add(bob);

        // Charlie has both the customer and employee role
        User charlie = new User();
        charlie.id(3);
        charlie.firstName("Charlie");
        charlie.lastName("Charhan");
        charlie.emailAddress("charlie@example.com");
        charlie.addRoleItem(UserRole.CUSTOMER);
        charlie.addRoleItem(UserRole.EMPLOYEE);
        charlie.phone("+31 6 12348765");
        charlie.transactionLimit(BigDecimal.valueOf(200f));
        charlie.dayLimit(0f);
        charlie.birthDate(LocalDate.of(1980, 8, 18));
        charlie.password("idk");

        users.add(charlie);

        expectedUsers = users;

        // accounts

        // account alice current
        Account accAliceCurrent = new Account();
        accAliceCurrent.balance(1000f);
        accAliceCurrent.accountType(AccountType.CURRENT);
        accAliceCurrent.IBAN("NL01INHO0000000002");
        accAliceCurrent.minimumLimit(50f);
        accAliceCurrent.userId(1);
        accounts.add(accAliceCurrent);
        accountsPerUser.add(accAliceCurrent);

        // account alice saving
        Account accAliceSave = new Account();
        accAliceSave.balance(1000f);
        accAliceSave.accountType(AccountType.SAVING);
        accAliceSave.IBAN("NL01INHO0000000003");
        accAliceSave.minimumLimit(50f);
        accAliceSave.userId(1);
        accounts.add(accAliceSave);
        accountsPerUser.add(accAliceSave);

        // account Bob current
        Account accBobCurrent = new Account();
        accBobCurrent.balance(50f);
        accBobCurrent.accountType(AccountType.CURRENT);
        accBobCurrent.IBAN("NL01INHO0000000004");
        accBobCurrent.minimumLimit(0f);
        accBobCurrent.userId(2);
        accounts.add(accBobCurrent);
        accountsPerCustomer.add(accBobCurrent);

        // account Bob saving
        Account accBobSave = new Account();
        accBobSave.balance(50f);
        accBobSave.accountType(AccountType.SAVING);
        accBobSave.IBAN("NL01INHO0000000005");
        accBobSave.minimumLimit(40f);
        accBobSave.userId(2);
        accounts.add(accBobSave);
        accountsPerCustomer.add(accBobSave);

        // account Charlie current
        Account accCharlieCurrent = new Account();
        accCharlieCurrent.balance(200f);
        accCharlieCurrent.accountType(AccountType.CURRENT);
        accCharlieCurrent.IBAN("NL01INHO0000000006");
        accCharlieCurrent.minimumLimit(50f);
        accCharlieCurrent.userId(3);
        accounts.add(accCharlieCurrent);
        accountsPerCustomerEmplyee.add(accCharlieCurrent);

        expectedAccountPerIban = accCharlieCurrent;
        expectedAccountsPerCustomer = accountsPerCustomer;
        expectedAccounts = accounts;
        expectedAccountsPerUser = accountsPerUser;
        expectedAccountsPerCustomerEmplyee = accountsPerCustomerEmplyee;
        // transaction

        // transaction alice
        Transaction transactionAliceTran = new Transaction();
        transactionAliceTran.setUserPerforming(1);
        transactionAliceTran.setTimestamp(OffsetDateTime.of(2020, 1, 1, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionAliceTran.setTransferTo("NL01INHO0000000002");
        transactionAliceTran.setTransferFrom("NL01INHO0000000003");
        transactionAliceTran.setAmount(50f);
        transactionAliceTran.setType(TransactionType.TRANSFER);
        transactions.add(transactionAliceTran);
        transactionsPerUser.add(transactionAliceTran);

        Transaction transactionAliceWithDraw = new Transaction();
        transactionAliceWithDraw.setUserPerforming(1);
        transactionAliceWithDraw.setTimestamp(OffsetDateTime.of(2020, 1, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionAliceWithDraw.setTransferTo("");
        transactionAliceWithDraw.setTransferFrom("NL01INHO0000000002");
        transactionAliceWithDraw.setAmount(50f);
        transactionAliceWithDraw.setType(TransactionType.WITHDRAW);
        transactions.add(transactionAliceWithDraw);
        transactionsPerUser.add(transactionAliceWithDraw);

        Transaction transactionAliceDep = new Transaction();
        transactionAliceDep.setUserPerforming(1);
        transactionAliceDep.setTimestamp(OffsetDateTime.of(2020, 1, 1, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionAliceDep.setTransferTo("NL01INHO0000000002");
        transactionAliceDep.setTransferFrom("");
        transactionAliceDep.setAmount(50f);
        transactionAliceDep.setType(TransactionType.DEPOSIT);
        transactions.add(transactionAliceDep);
        transactionsPerUser.add(transactionAliceDep);

        // transaction bob
        Transaction transactionBobTran = new Transaction();
        transactionBobTran.setUserPerforming(2);
        transactionBobTran.setTimestamp(OffsetDateTime.of(2020, 1, 1, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionBobTran.setTransferTo("NL01INHO0000000004");
        transactionBobTran.setTransferFrom("NL01INHO0000000002");
        transactionBobTran.setAmount(50f);
        transactionBobTran.setType(TransactionType.TRANSFER);
        transactions.add(transactionBobTran);
        transactionsByCustomerCurrent.add(transactionBobTran);

        // transaction charlie
        Transaction transactionCharlieTran = new Transaction();
        transactionCharlieTran.setUserPerforming(3);
        transactionCharlieTran.setTimestamp(OffsetDateTime.of(2020, 1, 1, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionCharlieTran.setTransferTo("NL01INHO0000000004");
        transactionCharlieTran.setTransferFrom("NL01INHO0000000006");
        transactionCharlieTran.setAmount(50f);
        transactionCharlieTran.setType(TransactionType.TRANSFER);
        transactions.add(transactionCharlieTran);
        transactionsByIBAN.add(transactionCharlieTran);
        transactionsByCustomerCurrent.add(transactionCharlieTran);

        // transaction charlie
        Transaction transactionCharlieTran2 = new Transaction();
        transactionCharlieTran2.setUserPerforming(3);
        transactionCharlieTran2.setTimestamp(OffsetDateTime.of(2020, 1, 1, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionCharlieTran2.setTransferTo("NL01INHO0000000006");
        transactionCharlieTran2.setTransferFrom("NL01INHO0000000007");
        transactionCharlieTran2.setAmount(50f);
        transactionCharlieTran2.setType(TransactionType.TRANSFER);
        transactions.add(transactionCharlieTran2);
        transactionsByIBAN.add(transactionCharlieTran2);

        expectedTransactions = transactions;
        expectedTransactionsByIBAN = transactionsByIBAN;
        expectedTransactionCurrentCustomer = transactionsByCustomerCurrent;

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(50f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000002");
        postTran.setTransferTo("NL01INHO0000000003");
        posts.add(postTran);

        PostTransBody postWith = new PostTransBody();
        postWith.setAmount(50f);
        postWith.setTransactionType(TransactionType.WITHDRAW);
        postWith.setTransferFrom("NL01INHO0000000006");
        postWith.setTransferTo("");
        posts.add(postWith);

        PostTransBody postDrop = new PostTransBody();
        postDrop.setAmount(50f);
        postDrop.setTransactionType(TransactionType.DEPOSIT);
        postDrop.setTransferFrom("");
        postDrop.setTransferTo("NL01INHO0000000006");
        posts.add(postDrop);

        expectedPostTran = posts;
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllTransactionsControllerShouldReturnListOfTransactionsForEmployee() throws Exception {
        // setup
        given(transactionRepository.findAll()).willReturn(expectedTransactions);
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedUsers.get(0).getId())).willReturn(expectedAccountsPerUser);

        // execute
        ResponseEntity<List<Transaction>> transactions = transactionsApiController.transactionsGet(50,0);

        // assertions
        assertNotNull(transactions);
        assertNotNull(transactions.getBody());
        assertEquals(6, transactions.getBody().size());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAllTransactionsControllerShouldReturnListOfTransactionsForCustomer() throws Exception {
        // setup
        for (Account account : expectedAccountsPerCustomer) {
            if (account.getAccountType() == AccountType.SAVING)
                given(transactionRepository.findByIban(account.getIBAN())).willReturn(expectedTransactionCurrentCustomer);
//            if (account.getAccountType() == AccountType.SAVING)
//                given(transactionRepository.findByIban(account.getIBAN())).willReturn(expectedTransactionCurrentCustomer);
        }
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAllByUserId(expectedUsers.get(1).getId())).willReturn(expectedAccountsPerCustomer);

        // execute
        ResponseEntity<List<Transaction>> transactions = transactionsApiController.transactionsGet(50,0);

        // assertions
        assertNotNull(transactions);
        assertNotNull(transactions.getBody());
        assertEquals(2, transactions.getBody().size());
        assertEquals(transactions.getBody().get(0), expectedTransactions.get(3));
    }

    @Test
    @WithMockUser(username = "charlie@example.com", authorities = { "CUSTOMER","EMPLOYEE" })
    public void getAllTransactionsControllerShouldReturnListOfTransactionsForEmployeeAlsoCustomer() throws Exception {
        // setup
        given(transactionRepository.findAll()).willReturn(expectedTransactions);
        for (Account account : expectedAccountsPerCustomer) {
            if (account.getAccountType() == AccountType.CURRENT)
                given(transactionRepository.findByIban(account.getIBAN())).willReturn(expectedTransactionCurrentCustomer);
            if (account.getAccountType() == AccountType.SAVING)
                given(transactionRepository.findByIban(account.getIBAN())).willReturn(expectedTransactionCurrentCustomer);
        }
        given(userRepository.findByEmailAddress(expectedUsers.get(2).getEmailAddress())).willReturn(expectedUsers.get(2));
        given(accountRepository.findAllByUserId(expectedUsers.get(2).getId())).willReturn(expectedAccountsPerCustomerEmplyee);

        // execute
        ResponseEntity<List<Transaction>> transactions = transactionsApiController.transactionsGet(50,0);


        // assertions
        assertNotNull(transactions);
        assertNotNull(transactions.getBody());
        assertEquals(6, transactions.getBody().size());
    }
//
    @Test
    @WithMockUser(username = "charlie@example.com", authorities = { "CUSTOMER","EMPLOYEE" })
    public void getAllTransactionsControllerByIbanShouldReturnListOfTransactionsByIban() throws Exception {
        // setup
        given(transactionRepository.findByIban(expectedAccounts.get(4).getIBAN())).willReturn(expectedTransactionsByIBAN);
        given(userRepository.findByEmailAddress(expectedUsers.get(2).getEmailAddress())).willReturn(expectedUsers.get(2));
        given(accountRepository.findAccountByIBAN(expectedAccounts.get(4).getIBAN())).willReturn(expectedAccountPerIban);

        // execute
//        List<Transaction> transactions = transactionService.getTransActionsByIBAN(expectedUsers.get(2).getEmailAddress(),expectedAccounts.get(4).getIBAN());
        ResponseEntity<List<Transaction>> transactions = transactionsApiController.transactionsIbanGet(expectedAccounts.get(4).getIBAN());

        // assertions
        assertNotNull(transactions);
        assertNotNull(transactions.getBody());
        assertEquals(2, transactions.getBody().size());
        assertEquals(expectedTransactionsByIBAN, transactions.getBody());
    }


    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void postTransactionControllerOnCurrentAccount() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedAccounts.get(0).getUserId())).willReturn(expectedAccountsPerUser);

        // execute
//        Transaction postTrans = transactionService.createTransaction();
        ResponseEntity postTrans = transactionsApiController.transactionsPost(expectedPostTran.get(0));

        // assertions
        assertNotNull(postTrans);
        assertEquals(HttpStatus.OK, postTrans.getStatusCode());
    }

    @Test
    @WithMockUser(username = "charlie@example.com", authorities = { "CUSTOMER","EMPLOYEE" })
    public void postWithdrawOnCurrentAccountController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(2).getEmailAddress())).willReturn(expectedUsers.get(2));
        given(accountRepository.findAllByUserId(expectedAccounts.get(4).getUserId())).willReturn(expectedAccountsPerCustomerEmplyee);

        // execute
        ResponseEntity postTrans = transactionsApiController.transactionsPost(expectedPostTran.get(1));

        // assertions
        assertNotNull(postTrans);
        assertEquals(HttpStatus.OK, postTrans.getStatusCode());
    }

    @Test
    @WithMockUser(username = "charlie@example.com", authorities = { "CUSTOMER","EMPLOYEE" })
    public void postDepositOnCurrentAccountController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(2).getEmailAddress())).willReturn(expectedUsers.get(2));
        given(accountRepository.findAllByUserId(expectedAccounts.get(4).getUserId())).willReturn(expectedAccountsPerCustomerEmplyee);

        // execute
        ResponseEntity postTrans = transactionsApiController.transactionsPost(expectedPostTran.get(2));

        // assertions
        assertNotNull(postTrans);
        assertEquals(HttpStatus.OK, postTrans.getStatusCode());
    }

    @Test
    @WithMockUser(username = "test@test.com", authorities = { "CUSTOMER" })
    public void getAllTransactionsControllerWithInvalidUserShouldThrowExceptionNotFound() {

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsGet(0,50));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("No authentication token was given.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllTransactionControllerWithInvalidOffsetShouldThrowException() {
        // setup
        given(transactionRepository.findAll()).willReturn(expectedTransactions);
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedUsers.get(0).getId())).willReturn(expectedAccountsPerUser);

        // Execution
        String email = expectedUsers.get(0).getEmailAddress();

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsGet(0,0));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Limit can not be below or eqeal 0, offset can not be below 0, offset can not be higher then limit", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllTransactionsControllerWithInvalidEmailAddressShouldThrowException() {
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));

        // Execution
        String iban = "LL01INHO0000000006";
        String email = expectedUsers.get(0).getEmailAddress();

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsIbanGet(iban));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The IBAN number is incorrect.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllTransactionsByIbanOfBankShouldThrowExceptionNotFound() {
        // Execution
        String iban = "NL01INHO0000000001";
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsIbanGet(iban));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("You can not access this account.", exception.getReason());
    }


    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getAllTransactionsByIbanWithInvalidRollShouldThrowExceptionController() {
        // given
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAccountByIBAN(expectedAccountsPerUser.get(0).getIBAN())).willReturn(expectedAccountsPerUser.get(0));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsIbanGet(expectedAccounts.get(0).getIBAN()));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("You are not authorized to this account.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void postTransactionMakeObjectWithInvalidWithdrawShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAccountByIBAN(expectedAccountsPerCustomer.get(0).getIBAN())).willReturn(expectedAccountsPerUser.get(0));
        expectedPostTran.get(1).setTransferFrom("fnw94ntnn4t");

        // assertions

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(expectedPostTran.get(1)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The IBAN to transfer from is incorrect.", exception.getReason());
        expectedPostTran.get(0).setTransferFrom("NL01INHO0000000002");
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void postTransactionMakeObjectWithInvalidDepositShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAccountByIBAN(expectedAccountsPerCustomer.get(0).getIBAN())).willReturn(expectedAccountsPerUser.get(0));
        expectedPostTran.get(2).setTransferTo("fnw94ntnn4t");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(expectedPostTran.get(2)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The IBAN to transfer to is incorrect.", exception.getReason());
        expectedPostTran.get(0).setTransferTo("NL01INHO0000000002");
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void postTransactionMakeObjectWithInvalidTransactionToShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAccountByIBAN(expectedAccountsPerCustomer.get(0).getIBAN())).willReturn(expectedAccountsPerUser.get(0));
        expectedPostTran.get(0).setTransferTo("fnw94ntnn4t");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(expectedPostTran.get(0)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The IBAN to transfer to is incorrect.", exception.getReason());
        expectedPostTran.get(0).setTransferTo("NL01INHO0000000002");
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void postTransactionMakeObjectWithInvalidTransactionFromShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAccountByIBAN(expectedAccountsPerCustomer.get(0).getIBAN())).willReturn(expectedAccountsPerUser.get(0));
        expectedPostTran.get(0).setTransferFrom("fnw94ntnn4t");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(expectedPostTran.get(0)));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The IBAN to transfer from is incorrect.", exception.getReason());
        expectedPostTran.get(0).setTransferFrom("NL01INHO0000000006");
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void postTransactionWithInvalidBalanceMinimumShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedAccountsPerUser.get(0).getUserId())).willReturn(expectedAccountsPerUser);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(1050f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000002");
        postTran.setTransferTo("NL01INHO0000000004");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(postTran));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The balance would fall below the minimum limit defined by user, change the limit or amount of the transaction.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void postTransactionWithInvalidFromToYouShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedAccountsPerUser.get(0).getUserId())).willReturn(expectedAccountsPerUser);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(1050f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000004");
        postTran.setTransferTo("NL01INHO0000000002");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(postTran));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("There is no account associated with the IBAN number to make the transaction.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void postTransactionWithInvalidUserMinimumLimitShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedAccountsPerUser.get(0).getUserId())).willReturn(expectedAccountsPerUser);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(975f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000002");
        postTran.setTransferTo("NL01INHO0000000004");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(postTran));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The balance would fall below the minimum limit defined by user, change the limit or amount of the transaction.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void postTransactionWithInvalidAmountMinimumLimitShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAllByUserId(expectedAccountsPerCustomer.get(0).getUserId())).willReturn(expectedAccountsPerCustomer);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(50f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000004");
        postTran.setTransferTo("NL01INHO0000000002");

        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(postTran));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("The amount of the transaction overseeded the limit of a transaction defined by the user, change the limit or amount of the transaction.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void postTransactionOnOtherSavingAccountShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAllByUserId(expectedAccountsPerCustomer.get(0).getUserId())).willReturn(expectedAccountsPerCustomer);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(5f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000005");
        postTran.setTransferTo("NL01INHO0000000006");

        given(accountRepository.findAccountByIBAN(postTran.getTransferTo())).willReturn(expectedAccounts.get(4));
        given(userRepository.findById(3)).willReturn(Optional.of(expectedUsers.get(2)));
        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(postTran));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("You can not transfer from your saving's account to someone else account.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void postTransactionOnYourSavingAccountShouldThrowExceptionController() throws Exception {
        // setup
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedAccountsPerUser.get(0).getUserId())).willReturn(expectedAccountsPerUser);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(5f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000002");
        postTran.setTransferTo("NL01INHO0000000005");

        given(accountRepository.findAccountByIBAN(postTran.getTransferTo())).willReturn(expectedAccounts.get(3));
        given(userRepository.findById(2)).willReturn(Optional.of(expectedUsers.get(1)));
        // assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionsApiController.transactionsPost(postTran));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("You can not transfer to a saving's account of someone else.", exception.getReason());
    }
}

package io.swagger.service;


import io.swagger.Swagger2SpringBoot;
import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes =  {Swagger2SpringBoot.class })
@AutoConfigureMockMvc
class TransactionServiceTest {


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
    Account expectedAccountPerIban;

    // test transaction
    @Autowired
    TransactionService transactionService;
    @MockBean
    TransactionRepository transactionRepository;

    List<Transaction> expectedTransactions;
    List<Transaction> expectedTransactionsPerUser;
    List<Transaction> expectedTransactionsByIBAN;
    List<Transaction> expectedTransactionSave;
    List<Transaction> expectedTransactionBob;

    @BeforeEach
    public void setup() {
        List<User> users = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        List<Account> accountsPerUser = new ArrayList<>();
        List<Account> accountsPerCustomer = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        List<Transaction> transactionsPerUser = new ArrayList<>();
        List<Transaction> transactionsByIBAN = new ArrayList<>();
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
        bob.transactionLimit(BigDecimal.valueOf(50f));
        bob.dayLimit(2000f);
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
        charlie.dayLimit(500f);
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
        accBobCurrent.minimumLimit(50f);
        accBobCurrent.userId(2);
        accounts.add(accBobCurrent);
        accountsPerCustomer.add(accBobCurrent);

        // account Bob saving
        Account accBobSave = new Account();
        accBobSave.balance(50f);
        accBobSave.accountType(AccountType.SAVING);
        accBobSave.IBAN("NL01INHO0000000005");
        accBobSave.minimumLimit(50f);
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
        expectedAccountPerIban = accCharlieCurrent;
        expectedAccountsPerCustomer = accountsPerCustomer;
        expectedAccounts = accounts;
        expectedAccountsPerUser = accountsPerUser;
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
        transactionsByIBAN.add(transactionBobTran);

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

        // transaction charlie
        Transaction transactionCharlieTran2 = new Transaction();
        transactionCharlieTran2.setUserPerforming(3);
        transactionCharlieTran2.setTimestamp(OffsetDateTime.of(2020, 1, 1, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionCharlieTran2.setTransferTo("NL01INHO0000000006");
        transactionCharlieTran2.setTransferFrom("NL01INHO0000000004");
        transactionCharlieTran2.setAmount(50f);
        transactionCharlieTran2.setType(TransactionType.TRANSFER);
        transactions.add(transactionCharlieTran2);
        transactionsByIBAN.add(transactionCharlieTran2);

        expectedTransactions = transactions;
        expectedTransactionsByIBAN = transactionsByIBAN;
    }

    @Test
    public void getAllTransactionsShouldReturnListOfTransactionsForEmployee() throws Exception {
        // setup
        given(transactionRepository.findAll()).willReturn(expectedTransactions);
        given(userRepository.findByEmailAddress(expectedUsers.get(0).getEmailAddress())).willReturn(expectedUsers.get(0));
        given(accountRepository.findAllByUserId(expectedUsers.get(0).getId())).willReturn(expectedAccountsPerUser);

        // execute
        List<Transaction> transactions = transactionService.getAllTransactions(0,50, expectedUsers.get(0).getEmailAddress());

        // assertions
        assertNotNull(transactions);
        assertEquals(6, transactions.size());
        assertEquals(transactions, expectedTransactions);
    }

    @Test
    public void getAllTransactionsShouldReturnListOfTransactionsForCustomer() throws Exception {
        // setup
        for (Account account : expectedAccountsPerCustomer) {
            given(transactionRepository.findByIban(account.getIBAN())).willReturn(expectedTransactionsByIBAN);
        }
        given(userRepository.findByEmailAddress(expectedUsers.get(1).getEmailAddress())).willReturn(expectedUsers.get(1));
        given(accountRepository.findAllByUserId(expectedUsers.get(1).getId())).willReturn(expectedAccountsPerUser);

        // execute
        List<Transaction> transactions = transactionService.getAllTransactions(0,50, expectedUsers.get(1).getEmailAddress());

        // assertions
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertEquals(transactions.get(0), expectedTransactions.get(3));
    }

    @Test
    public void getAllTransactionsByIbanShouldReturnListOfTransactionsByIban() throws Exception {
        // setup
        given(transactionRepository.findByIban(expectedAccounts.get(4).getIBAN())).willReturn(expectedTransactionsByIBAN);
        given(userRepository.findByEmailAddress(expectedUsers.get(2).getEmailAddress())).willReturn(expectedUsers.get(2));
        given(accountRepository.findAccountByIBAN(expectedAccounts.get(4).getIBAN())).willReturn(expectedAccountPerIban);

        // execute
        List<Transaction> transactions = transactionService.getTransActionsByIBAN(expectedUsers.get(2).getEmailAddress(),expectedAccounts.get(4).getIBAN());

        // assertions
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertEquals(transactions, expectedTransactionsByIBAN);
    }

}

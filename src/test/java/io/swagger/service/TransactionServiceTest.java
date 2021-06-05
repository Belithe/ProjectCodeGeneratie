package io.swagger.service;


import io.swagger.Swagger2SpringBoot;
import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    // test transaction
    @Autowired
    TransactionService transactionService;
    @MockBean
    TransactionRepository transactionRepository;
    List<Transaction> expectedTransaction;

    @BeforeEach
    public void setup() {
        List<User> users = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

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

        // account alice saving
        Account accAliceSave = new Account();
        accAliceSave.balance(1000f);
        accAliceSave.accountType(AccountType.SAVING);
        accAliceSave.IBAN("NL01INHO0000000003");
        accAliceSave.minimumLimit(50f);
        accAliceSave.userId(1);
        accounts.add(accAliceSave);

        // account Bob current
        Account accBobCurrent = new Account();
        accBobCurrent.balance(50f);
        accBobCurrent.accountType(AccountType.CURRENT);
        accBobCurrent.IBAN("NL01INHO0000000004");
        accBobCurrent.minimumLimit(50f);
        accBobCurrent.userId(2);
        accounts.add(accBobCurrent);

        // account Bob saving
        Account accBobSave = new Account();
        accBobSave.balance(50f);
        accBobSave.accountType(AccountType.SAVING);
        accBobSave.IBAN("NL01INHO0000000005");
        accBobSave.minimumLimit(50f);
        accBobSave.userId(2);
        accounts.add(accBobSave);

        // account Charlie current
        Account accCharlieCurrent = new Account();
        accCharlieCurrent.balance(200f);
        accCharlieCurrent.accountType(AccountType.CURRENT);
        accCharlieCurrent.IBAN("NL01INHO0000000006");
        accCharlieCurrent.minimumLimit(50f);
        accCharlieCurrent.userId(3);
        accounts.add(accCharlieCurrent);

        expectedAccounts = accounts;

        // transaction

        Transaction transactionAlice = new Transaction();
        transactionAlice.setUserPerforming(1);
        transactionAlice.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transactionAlice.setTransferTo("NL01INHO0000000002");
        transactionAlice.setTransferFrom("NL01INHO0000000003");
        transactionAlice.setAmount(50f);
        transactionAlice.setType(TransactionType.TRANSFER);
    }
}

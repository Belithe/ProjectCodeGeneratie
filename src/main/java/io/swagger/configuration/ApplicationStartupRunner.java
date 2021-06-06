package io.swagger.configuration;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountManagementService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

@Profile("!unitTesting")
@Component
public class ApplicationStartupRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Alice is just an employee
        User alice = new User();
        alice.firstName("Alice");
        alice.lastName("Alixon");
        alice.emailAddress("alice@example.com");
        alice.addRoleItem(UserRole.EMPLOYEE);
        alice.phone("+31 6 12345678");
        alice.transactionLimit(BigDecimal.valueOf(100f));
        alice.dayLimit(1000f);
        alice.birthDate(LocalDate.of(2010, 10, 10));
        alice.password("idk");

        userService.add(alice);

        // Charlie is just a customer
        User bob = new User();
        bob.firstName("Bob");
        bob.lastName("Bobson");
        bob.emailAddress("bob@example.com");
        bob.addRoleItem(UserRole.CUSTOMER);
        bob.phone("+31 6 87654321");
        bob.transactionLimit(BigDecimal.valueOf(50f));
        bob.dayLimit(2000f);
        bob.birthDate(LocalDate.of(2012, 12, 12));
        bob.password("idk");

        userService.add(bob);

        // Charlie has both the customer and employee role
        User charlie = new User();
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

        userService.add(charlie);

        // Add account for
        // Bank

        Account bank = new Account();
        bank.setBalance(0f);
        bank.setAccountType(AccountType.CURRENT);
        bank.setIBAN("NL01INHO0000000001");
        bank.setMinimumLimit(0f);
        bank.setUserId(null);
        accountRepository.save(bank);

        Account account = new Account();
        account.setBalance(1000f);
        account.setAccountType(AccountType.CURRENT);
        account.setIBAN("NL01INHO0000000002");
        account.setMinimumLimit(50f);
        account.setUserId(2);
        accountRepository.save(account);

        Account accountBobSave = new Account();
        accountBobSave.setBalance(1000f);
        accountBobSave.setAccountType(AccountType.CURRENT);
        accountBobSave.setIBAN("NL01INHO0000000003");
        accountBobSave.setMinimumLimit(50f);
        accountBobSave.setUserId(2);
        accountRepository.save(accountBobSave);

        Account accountSave = new Account();
        accountSave.setBalance(1000f);
        accountSave.setAccountType(AccountType.SAVING);
        accountSave.setIBAN("NL01INHO0000000004");
        accountSave.setMinimumLimit(50f);
        accountSave.setUserId(3);
        accountRepository.save(accountSave);

        PostTransBody postTran = new PostTransBody();
        postTran.setAmount(50f);
        postTran.setTransactionType(TransactionType.TRANSFER);
        postTran.setTransferFrom("NL01INHO0000000002");
        postTran.setTransferTo("NL01INHO0000000003");

        PostTransBody postWith = new PostTransBody();
        postWith.setAmount(50f);
        postWith.setTransactionType(TransactionType.WITHDRAW);
        postWith.setTransferFrom("NL01INHO0000000002");
        postWith.setTransferTo("");

        PostTransBody postDrop = new PostTransBody();
        postDrop.setAmount(50f);
        postDrop.setTransactionType(TransactionType.DEPOSIT);
        postDrop.setTransferFrom("");
        postDrop.setTransferTo("NL01INHO0000000002");

        transactionService.createTransaction(bob.getEmailAddress(), postTran);

        transactionService.createTransaction(bob.getEmailAddress(), postWith);

        transactionService.createTransaction(charlie.getEmailAddress(), postDrop);
    }
}


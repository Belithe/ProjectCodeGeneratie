package io.swagger;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.repository.AccountRepository;
import io.swagger.service.AccountManagementService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneOffset;
import springfox.documentation.oas.annotations.EnableOpenApi;
import org.threeten.bp.OffsetDateTime;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class TestConsole implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountManagementService accountManagementService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(String ...args) throws Exception {

    }

    public void testUser() {
        User user = new User();
        user.birthDate(LocalDate.of(2000, 1, 1));
        user.dayLimit(100f);
        user.emailAddress("test@test.com");
        user.lastName("Alixon");
        user.firstName("Alice");
        user.phone("+31 6 12345678");
        user.setRole(Collections.singletonList(UserRole.CUSTOMER));
        user.id(10);
        user.password("idk");
        userService.add(user);
    }


    private void addTransAction() {
        Transaction transaction = new Transaction();
        transaction.setUserPerforming(6);
        transaction.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transaction.setTransferTo("NL01INHO0000000002");
        transaction.setTransferFrom("NL01INHO0000000003");
        transaction.setAmount((float) 50);
        transaction.setType(TransactionType.TRANSFER);
        transactionRepository.save(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setUserPerforming(1);
        transaction2.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transaction2.setTransferTo("NL01INHO0000000003");
        transaction2.setTransferFrom("NL01INHO0000000002");
        transaction2.setAmount((float) 50);
        transaction2.setType(TransactionType.TRANSFER);
        transactionRepository.save(transaction2);
    }

    public void printAccount() {
        Account toPrint = accountManagementService.getByIBAN("NL01INHO0000000001");

        System.out.println(toPrint.getBalance());
        System.out.println(toPrint.getMinimumLimit());
        System.out.println(toPrint.getIBAN());
        System.out.println(toPrint.getUserId());
    }

    public void testAccount() {
        CreateAccountPostBody accountToTest = new CreateAccountPostBody();
        accountToTest.setUserId(10);
        accountToTest.setIBAN("NL01INHO0000000001");
        accountToTest.setMinimumLimit(200.0F);
        accountToTest.setAccountType(AccountType.SAVING);

        accountManagementService.createNewAccount(accountToTest);

    }

    public static void main(String[] args) throws Exception {
        new SpringApplication(TestConsole.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}

package io.swagger;

import io.swagger.model.*;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import springfox.documentation.oas.annotations.EnableOpenApi;

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



    @Override
    public void run(String ...args) throws Exception {
//        Transaction transaction = new Transaction();
//        transaction.setTransactionId(1);
//        transaction.setUserPerforming(1);
//        transaction.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
//        transaction.setTransferTo("NL01INHO0000000002");
//        transaction.setTransferFrom("NL01INHO0000000003");
//        transaction.setAmount((float) 50);
//        transaction.setType(TransactionType.TRANSFER);
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setTransactionId(2);
//        transaction2.setUserPerforming(1);
//        transaction2.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
//        transaction2.setTransferTo("NL01INHO0000000003");
//        transaction2.setTransferFrom("NL01INHO0000000002");
//        transaction2.setAmount((float) 50);
//        transaction2.setType(TransactionType.TRANSFER);

//        User user = new User();
//        user.birthDate(LocalDate.of(2000, 1, 1));
//        user.dayLimit(100f);
//        user.emailAddress("test@test.com");
//        user.lastName("Alixon");
//        user.firstName("Alice");
//        user.phone("+31 6 12345678");
//        user.setRole(Collections.singletonList(UserRole.CUSTOMER));
//        user.id(10);
//        user.password("idk");
//        userService.add(user);
//
//        transactionService.testAddTransaction();
//
//        List<Transaction> transactions = transactionService.getAllTransactions(1,1, "test@test.com");
//        for (Transaction t : transactions) {
//         System.out.println(t.toString());
//        }
//        List<User> users = userService.getAllUsers();
//        User i = users.get(0);
//        transactionRepository.save(transaction);
//
//        Body1 b = new Body1();
//
//        b.firstName("Bob");
//        b.lastName("Bonson");
//        userService.updateUserById(i.getId(), b);
//
//        List<User> users2 = userService.getAllUsers();
//        for (User u : users2) {
//            System.out.println(u.getFirstName());
//        }
//        Transaction i = transactionRepository.findTransactionByTransferTo("Hello");
//        System.out.println(i.getTransactionId());

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

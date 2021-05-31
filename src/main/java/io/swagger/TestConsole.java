package io.swagger;

import io.swagger.model.Body1;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.TransactionRepository;
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

import java.util.List;

@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class TestConsole implements CommandLineRunner {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void run(String ...args) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(OffsetDateTime.of(2020, 01, 01, 10, 10, 10, 0, ZoneOffset.UTC));
        transaction.setTransactionId(42);
        transaction.setTransferFrom("HI");
        transaction.setTransferTo("Hello");

        transactionRepository.save(transaction);

        Transaction i = transactionRepository.findTransactionByTransferTo("Hello");
        System.out.println(i.getTransactionId());
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

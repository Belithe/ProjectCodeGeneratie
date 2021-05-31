package io.swagger;

import io.swagger.model.Body1;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
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
import java.util.List;

@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class TestConsole implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(String ...args) throws Exception {
        User user = new User();
        user.id(10);
        user.firstName("Alice");
        user.lastName("Alixon");
        user.emailAddress("alice@example.com");
        user.addRoleItem(UserRole.EMPLOYEE);
        user.phone("+31 6 12345678");
        user.transactionLimit(BigDecimal.valueOf(100f));
        user.dayLimit(1000f);
        user.birthDate(LocalDate.of(2010, 10, 10));
        user.password("idk");

        userService.add(user);
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

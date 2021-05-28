package io.swagger;

import io.swagger.model.Body1;
import io.swagger.model.User;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.threeten.bp.LocalDate;
import springfox.documentation.oas.annotations.EnableOpenApi;

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
        user.birthDate(LocalDate.of(2000, 1, 1));
        user.dayLimit(100f);
        user.emailAddress("alice@example.com");
        user.lastName("Alixon");
        user.firstName("Alice");
        user.phone("+31 6 12345678");
        user.role(User.RoleEnum.EMPLOYEE);
        user.id(10);
        user.setPassword("testing");

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

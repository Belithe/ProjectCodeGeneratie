package io.swagger.configuration;

import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

@Component
public class ApplicationStartupRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

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
    }
}


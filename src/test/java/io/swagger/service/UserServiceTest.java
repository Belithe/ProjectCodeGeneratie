package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { Swagger2SpringBoot.class })
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    public void addUsersToDB() {
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

    @Test
    public void test() {
        
    }

//    @Test
//    public UserService makeUserService() {
//        UserService userService = new UserService();
//        return userService;
//    }
//
//
//
//    //TESTS
//    @Test
//    public void userServiceIsNotNull() {
//        UserService userService = new UserService();
//        assertNotNull(userService);
//    }
//
//    @Test
//    public void userLoginCheckEmailFormatValid() {
//        String emailAddress = "test@gmail.com";
//        Boolean validEmailaddress = makeUserService().checkValidEmailaddress(emailAddress);
//        System.out.println("Emailaddress [" + emailAddress + "] is valid: " + validEmailaddress);
//    }
//
//
//    @Test
//    public void userLoginCheckEmail() {
//        String emailAddress = "test@gmail.com";
//        String password = "p@ssw0rd";
//        //String token = makeUserService().login(emailAddress, password);
//        //System.out.println(token);
//    }
//
//    @Test
//    public void checkIfLoginCheckWorksAndGetToken() {
//        String emailAddress = "testus";
//        String password = "test";
//        //String token = makeUserService().login(emailAddress, password);
//        //System.out.println(token);
//    }
//
//    @Test
//    public void createANewUser(){
//        UserService userService = makeUserService();
//    }

}
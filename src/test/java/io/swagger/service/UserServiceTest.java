package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.api.UsersApiController;
import io.swagger.model.Body1;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { Swagger2SpringBoot.class })
//@RunWith(SpringRunner.class)
//@WebMvcTest(UsersApiController.class)
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    Body1 createUserBody;

    @BeforeEach
    public void initializeCreateUserBody() {
        createUserBody = new Body1();
        createUserBody.firstName("Alice");
        createUserBody.lastName("Alixon");
        createUserBody.emailAddress("aliceexample.com");
        createUserBody.addRoleItem(UserRole.EMPLOYEE);
        createUserBody.phone("+31 6 12345678");
        createUserBody.transactionLimit(BigDecimal.valueOf(100f));
        createUserBody.dayLimit(1000f);
        createUserBody.birthDate(LocalDate.of(2010, 10, 10));
        createUserBody.password("idk"); // shhhhh...
    }

    @Test
    public void validateEmailAddressIncorrect() throws Exception {
        mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" +
                "  \"birthDate\": \"1800-06-02\",\n" +
                "  \"dayLimit\": 0,\n" +
                "  \"emailAddress\": \"cheeyau@example\",\n" +
                "  \"firstName\": \"Cheeyau\",\n" +
                "  \"lastName\": \"Au\",\n" +
                "  \"password\": \"idk\",\n" +
                "  \"phone\": \"+31 6 12345678\",\n" +
                "  \"role\": [\n" +
                "    \"customer\"\n" +
                "  ],\n" +
                "  \"transactionLimit\": 100\n" +
                "}"))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    public void validateEmailAddressCorrect() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"birthDate\": \"1800-06-02\",\n" +
                        "  \"dayLimit\": 0,\n" +
                        "  \"emailAddress\": \"cheeyau@example.com\",\n" +
                        "  \"firstName\": \"Cheeyau\",\n" +
                        "  \"lastName\": \"Au\",\n" +
                        "  \"password\": \"idk\",\n" +
                        "  \"phone\": \"+31 6 12345678\",\n" +
                        "  \"role\": [\n" +
                        "    \"customer\"\n" +
                        "  ],\n" +
                        "  \"transactionLimit\": 100\n" +
                        "}"))
                .andExpect(
                        status().isBadRequest()
                );
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
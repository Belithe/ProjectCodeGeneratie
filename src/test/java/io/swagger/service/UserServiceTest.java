package io.swagger.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    public UserService makeUserService() {
        UserService userService = new UserService();
        return userService;
    }



    //TESTS
    @Test
    public void userServiceIsNotNull() {
        UserService userService = new UserService();
        assertNotNull(userService);
    }

    @Test
    public void userLoginCheckEmailFormatValid() {
        String emailAddress = "test@gmail.com";
        Boolean validEmailaddress = makeUserService().checkValidEmailaddress(emailAddress);
        System.out.println("Emailaddress [" + emailAddress + "] is valid: " + validEmailaddress);
    }


    @Test
    public void userLoginCheckEmail() {
        String emailAddress = "test@gmail.com";
        String password = "p@ssw0rd";
        //String token = makeUserService().login(emailAddress, password);
        //System.out.println(token);
    }

    @Test
    public void checkIfLoginCheckWorksAndGetToken() {
        String emailAddress = "testus";
        String password = "test";
        //String token = makeUserService().login(emailAddress, password);
        //System.out.println(token);
    }

    @Test
    public void createANewUser(){
        UserService userService = makeUserService();
    }

}
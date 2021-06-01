package io.swagger.configuration;

import io.swagger.model.User;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ApplicationStartupRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User testUser = new User();
        testUser.setEmailAddress("testus");
        testUser.setPassword("test");
        //testUser.setRole(Role.ROLE_EMPLOYEE);
        userService.add(testUser);
    }
}


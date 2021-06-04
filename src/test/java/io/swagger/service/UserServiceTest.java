package io.swagger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import io.swagger.Swagger2SpringBoot;
import io.swagger.api.UsersApiController;
import io.swagger.model.CreateUserPostBody;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.model.dto.LoginResponseDTO;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { Swagger2SpringBoot.class })
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    List<User> expectedUsers;

    @BeforeEach
    public void setup() {
        List<User> users = new ArrayList<>();

        // Alice is just an employee
        User alice = new User();
        alice.id(1);
        alice.firstName("Alice");
        alice.lastName("Alixon");
        alice.emailAddress("alice@example.com");
        alice.addRoleItem(UserRole.EMPLOYEE);
        alice.phone("+31 6 12345678");
        alice.transactionLimit(BigDecimal.valueOf(100f));
        alice.dayLimit(1000f);
        alice.birthDate(LocalDate.of(2010, 10, 10));
        alice.password("idk");

        users.add(alice);

        // Charlie is just a customer
        User bob = new User();
        bob.id(2);
        bob.firstName("Bob");
        bob.lastName("Bobson");
        bob.emailAddress("bob@example.com");
        bob.addRoleItem(UserRole.CUSTOMER);
        bob.phone("+31 6 87654321");
        bob.transactionLimit(BigDecimal.valueOf(50f));
        bob.dayLimit(2000f);
        bob.birthDate(LocalDate.of(2012, 12, 12));
        bob.password("idk");

        users.add(bob);

        // Charlie has both the customer and employee role
        User charlie = new User();
        charlie.id(3);
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

        users.add(charlie);

        expectedUsers = users;
    }

    @Test
    public void getAllUsers() {
        // Setup
        given(userRepository.findAll()).willReturn(expectedUsers);

        // Execution
        List<User> users = userService.getAllUsers();

        // Assertions
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals(users, expectedUsers);
    }

    @Test
    public void getUserById() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        User user = userService.getUserById(1);

        // Assertions
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals(expectedUsers.get(0), user);
    }

    @Test
    public void getNonExistingUserById() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // Execution
        User user = userService.getUserById(1);

        // Assertions
        assertNull(user);
    }


}
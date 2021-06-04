package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.api.UsersApiController;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = { Swagger2SpringBoot.class })
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    UsersApiController usersApiController;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllUsers() {
        // Setup
        given(userService.getAllUsers()).willReturn(expectedUsers);

        // Execution
        ResponseEntity<List<User>> usersResponse = usersApiController.usersGet(20, 1);

        // Assertions
        assertNotNull(usersResponse);
        assertNotNull(usersResponse.getBody());
        assertEquals(3, usersResponse.getBody().size());
        assertEquals(expectedUsers, usersResponse.getBody());
    }

    @Test
    public void getAllUsersWithoutAuthentication() {
        // Setup
        given(userService.getAllUsers()).willReturn(expectedUsers);

        // Assertions
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> usersApiController.usersGet(20, 1));
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "CUSTOMER" })
    public void getAllUsersWithoutProperAuthorization() {
        // Setup
        given(userService.getAllUsers()).willReturn(expectedUsers);

        // Assertion
        assertThrows(AccessDeniedException.class, () -> usersApiController.usersGet(20, 1));
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getUserByIdPerformedByEmployee() {
        // Setup
        given(userService.getUserById(1)).willReturn(expectedUsers.get(0));
        given(userService.getUserByEmailAddress("alice@example.com")).willReturn(expectedUsers.get(0));

        // Execution
        ResponseEntity<User> userResponse = usersApiController.usersUserIdGet(1);

        // Assertions
        assertNotNull(userResponse);
        assertNotNull(userResponse.getBody());
        assertEquals(1, userResponse.getBody().getId());
        assertEquals(expectedUsers.get(0), userResponse.getBody());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getUserByIdPerformedBySameUserAsRequestedUser() {
        // Setup
        given(userService.getUserById(2)).willReturn(expectedUsers.get(1));
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        ResponseEntity<User> userResponse = usersApiController.usersUserIdGet(2);

        // Assertions
        assertNotNull(userResponse);
        assertNotNull(userResponse.getBody());
        assertEquals(2, userResponse.getBody().getId());
        assertEquals(expectedUsers.get(1), userResponse.getBody());
    }

    @Test
    @WithMockUser(username = "anonymousUser", authorities = { "ROLE_ANONYMOUS" })
    public void getUserByIdWithoutAuthentication() {
        // Setup
        given(userService.getUserById(1)).willReturn(expectedUsers.get(1));

        // Assertions
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> usersApiController.usersUserIdGet(1));
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getUserByIdWithoutProperAuthorization() {
        // Setup
        given(userService.getUserById(1)).willReturn(expectedUsers.get(1));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdGet(1));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("No authentication token was given.", exception.getReason());
    }
}

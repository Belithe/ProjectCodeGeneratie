package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.api.UsersApiController;
import io.swagger.model.CreateUserPostBody;
import io.swagger.model.UpdateUserPutBody;
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
import java.util.stream.Collectors;

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
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllUsersLimited() {
        // Setup
        given(userService.getAllUsers()).willReturn(expectedUsers);

        // Execution
        ResponseEntity<List<User>> usersResponse = usersApiController.usersGet(2, 1);

        // Assertions
        assertNotNull(usersResponse);
        assertNotNull(usersResponse.getBody());
        assertEquals(2, usersResponse.getBody().size());

        // Need to compare each item as the lists are not the same instance
        List<User> expectedLimitedUsers = expectedUsers.subList(0, 1);
        for (int i = 0; i < expectedLimitedUsers.size(); i++) {
            User expectedUser = expectedLimitedUsers.get(i);
            User respondedUser = usersResponse.getBody().get(i);

            assertEquals(expectedUser, respondedUser);
        }
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllUsersLimitedSecondPage() {
        // Setup
        given(userService.getAllUsers()).willReturn(expectedUsers);

        // Execution
        ResponseEntity<List<User>> usersResponse = usersApiController.usersGet(2, 2);

        // Assertions
        assertNotNull(usersResponse);
        assertNotNull(usersResponse.getBody());
        assertEquals(1, usersResponse.getBody().size());

        // Need to compare each item as the lists are not the same instance
        List<User> expectedLimitedUsers = expectedUsers.subList(2, 2);
        for (int i = 0; i < expectedLimitedUsers.size(); i++) {
            User expectedUser = expectedLimitedUsers.get(i);
            User respondedUser = usersResponse.getBody().get(i);

            assertEquals(expectedUser, respondedUser);
        }
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getAllUsersLimitedThirdPage() {
        // Setup
        given(userService.getAllUsers()).willReturn(expectedUsers);

        // Execution
        ResponseEntity<List<User>> usersResponse = usersApiController.usersGet(2, 3);

        // Assertions
        assertNotNull(usersResponse);
        assertNotNull(usersResponse.getBody());
        assertEquals(0, usersResponse.getBody().size());
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
    @WithMockUser(username = "anonymousUser", authorities = { "ROLE_ANONYMOUS" })
    public void getUserByIdWithoutAuthentication() {
        // Setup
        given(userService.getUserById(1)).willReturn(expectedUsers.get(1));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdGet(1));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("No authentication token was given.", exception.getReason());
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
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
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
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void getUserByIdWithoutProperAuthorization() {
        // Setup
        given(userService.getUserById(1)).willReturn(expectedUsers.get(1));
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdGet(1));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("The current auth token does not provide access to this resource.", exception.getReason());
    }

    private CreateUserPostBody createBaseUserPostBody() {
        CreateUserPostBody createUserPostBody = new CreateUserPostBody();
        createUserPostBody.setFirstName("Test");
        createUserPostBody.setLastName("Testosterone");
        createUserPostBody.setEmailAddress("test@example.com");
        createUserPostBody.addRoleItem(UserRole.CUSTOMER);
        createUserPostBody.setPhone("+31 6 87654321");
        createUserPostBody.setTransactionLimit(BigDecimal.valueOf(200f));
        createUserPostBody.setDayLimit(3000f);
        createUserPostBody.setBirthDate(LocalDate.of(2020, 12, 20));
        createUserPostBody.setPassword("idk");

        return createUserPostBody;
    }

    @Test
    public void createUserWithoutAuthentication() {
        // Execution
        CreateUserPostBody createUserPostBody = createBaseUserPostBody();

        // Assertions
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> usersApiController.usersPost(createUserPostBody));
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void createUserPerformedByEmployee() {
        // Execution
        CreateUserPostBody createUserPostBody = createBaseUserPostBody();
        ResponseEntity<User> response = usersApiController.usersPost(createUserPostBody);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void createUserPerformedByCustomer() {
        // Execution
        CreateUserPostBody createUserPostBody = createBaseUserPostBody();

        // Assertions
        assertThrows(AccessDeniedException.class, () -> usersApiController.usersPost(createUserPostBody));
    }

    @Test
    public void deleteUserWithoutAuthentication() {
        // Execution
        CreateUserPostBody createUserPostBody = createBaseUserPostBody();

        // Assertions
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> usersApiController.usersUserIdDelete(1));
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void deleteUserPerformedByEmployee() {
        // Execution
        ResponseEntity<Void> response = usersApiController.usersUserIdDelete(1);

        // Assertions
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void deleteUserPerformedByCustomer() {
        // Assertions
        assertThrows(AccessDeniedException.class, () -> usersApiController.usersUserIdDelete(1));
    }

    @Test
    @WithMockUser(username = "anonymousUser", authorities = { "ROLE_ANONYMOUS" })
    public void updateUserWithoutAuthentication() {
        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(1, userPutBody));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("No authentication token was given.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void updateUserPerformedByEmployee() {
        // Setup
        given(userService.getUserByEmailAddress("alice@example.com")).willReturn(expectedUsers.get(0));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();

        // Assertions
        ResponseEntity<Void> response = usersApiController.usersUserIdPut(1, userPutBody);
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomer() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();

        // Assertions
        ResponseEntity<Void> response = usersApiController.usersUserIdPut(2, userPutBody);
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByOtherCustomer() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(1, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("The current auth token does not provide access to this resource.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomerDayLimitShouldThrowException() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setDayLimit(10f);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(2, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customers cannot change their own day limit.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomerTransactionLimitShouldThrowException() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setTransactionLimit(BigDecimal.valueOf(10));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(2, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customers cannot change their own transaction limit.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomerRoleShouldThrowException() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.addRoleItem(UserRole.EMPLOYEE);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(2, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customers cannot change their own role.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomerBirthDateShouldThrowException() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setBirthDate(LocalDate.of(2010, 10, 10));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(2, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customers cannot change their own birthdate.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomerFirstnameShouldThrowException() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setFirstName("Test");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(2, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customers cannot change their firstname of their own.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "bob@example.com", authorities = { "CUSTOMER" })
    public void updateUserPerformedByCustomerLastnameShouldThrowException() {
        // Setup
        given(userService.getUserByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(1));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setLastName("Test");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersUserIdPut(2, userPutBody));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Customers cannot change their lastname of their own.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "anonymousUser", authorities = { "ROLE_ANONYMOUS" })
    public void getSelfUserWithoutAuthentication() {
        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> usersApiController.usersSelfGet());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("No authentication token was given.", exception.getReason());
    }

    @Test
    @WithMockUser(username = "alice@example.com", authorities = { "EMPLOYEE" })
    public void getSelfUser() {
        // Setup
        given(userService.getUserByEmailAddress("alice@example.com")).willReturn(expectedUsers.get(0));

        // Execution
        ResponseEntity<User> response = usersApiController.usersSelfGet();

        // Assertions
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }
}

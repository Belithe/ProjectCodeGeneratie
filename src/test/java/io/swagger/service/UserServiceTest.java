package io.swagger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import io.swagger.Swagger2SpringBoot;
import io.swagger.api.UsersApiController;
import io.swagger.model.CreateUserPostBody;
import io.swagger.model.UpdateUserPutBody;
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
import org.springframework.http.HttpStatus;
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
    public void getAllUsersShouldReturnListOfUsers() {
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
    public void getUserByIdShouldReturnUser() {
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
    public void getNonExistingUserByIdShouldReturnNull() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // Execution
        User user = userService.getUserById(1);

        // Assertions
        assertNull(user);
    }

    @Test
    public void getUserByEmailAddress() {
        // Setup
        given(userRepository.findByEmailAddress("alice@example.com")).willReturn(expectedUsers.get(0));

        // Execution
        User user = userService.getUserByEmailAddress("alice@example.com");

        // Assertions
        assertNotNull(user);
        assertEquals("alice@example.com", user.getEmailAddress());
    }

    @Test
    public void getNonExistingUserByEmailAddressShouldReturnNull() {
        // Setup
        given(userRepository.findByEmailAddress("alice@example.com")).willReturn(null);

        // Execution
        User user = userService.getUserByEmailAddress("alice@example.com");

        // Assertions
        assertNull(user);
    }

    @Test
    public void updateUserById() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setFirstName("Test");
        userPutBody.setLastName("Testosterone");
        userPutBody.setEmailAddress("test@example.com");
        userPutBody.addRoleItem(UserRole.CUSTOMER);
        userPutBody.setPhone("+31 6 87654321");
        userPutBody.setTransactionLimit(BigDecimal.valueOf(200f));
        userPutBody.setDayLimit(3000f);
        userPutBody.setBirthDate(LocalDate.of(2020, 12, 20));
        userPutBody.setPassword("idk");

        User user = userService.updateUserById(1, userPutBody);

        // Assertions
        assertNotNull(user);
        assertEquals(userPutBody.getFirstName(), user.getFirstName());
        assertEquals(userPutBody.getLastName(), user.getLastName());
        assertEquals(userPutBody.getEmailAddress(), user.getEmailAddress());
        assertEquals(userPutBody.getRole(), user.getRole());
        assertEquals(userPutBody.getPhone(), user.getPhone());
        assertEquals(userPutBody.getTransactionLimit(), user.getTransactionLimit());
        assertEquals(userPutBody.getDayLimit(), user.getDayLimit());
        assertEquals(userPutBody.getBirthDate(), user.getBirthDate());
    }

    @Test
    public void updateNonExistingUserByIdShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setFirstName("Test");
        userPutBody.setLastName("Testosterone");
        userPutBody.setEmailAddress("test@example.com");
        userPutBody.addRoleItem(UserRole.CUSTOMER);
        userPutBody.setPhone("+31 6 87654321");
        userPutBody.setTransactionLimit(BigDecimal.valueOf(200f));
        userPutBody.setDayLimit(3000f);
        userPutBody.setBirthDate(LocalDate.of(2020, 12, 20));
        userPutBody.setPassword("idk");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals("Could not find an user with the given user ID.", exception.getReason());
    }

    @Test
    public void updateUserWithInvalidEmailAddressShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setEmailAddress("ijqfefq.com");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Invalid email address given.", exception.getReason());
    }

    @Test
    public void updateUserWithInUseEmailAddressShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));
        given(userRepository.findByEmailAddress("bob@example.com")).willReturn(expectedUsers.get(0));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setEmailAddress("bob@example.com");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Email address is already in use.", exception.getReason());
    }

    @Test
    public void updateUserWithoutRolesShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setRole(new ArrayList<>());

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("User should have at least one valid role.", exception.getReason());
    }

    @Test
    public void updateUserWithInvalidRoleShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.addRoleItem(null);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Roles field contains invalid value.", exception.getReason());
    }

    @Test
    public void updateUserWithDuplicateRoleShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.addRoleItem(UserRole.EMPLOYEE);
        userPutBody.addRoleItem(UserRole.EMPLOYEE);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Roles field contains duplicate values.", exception.getReason());
    }

    @Test
    public void updateUserWithValueOfDayLimitLessThanZeroShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setDayLimit(-10f);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Cannot set day limit to a value less than 0.", exception.getReason());
    }

    @Test
    public void updateUserWithValueOfTransactionLimitLessThanZeroShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        UpdateUserPutBody userPutBody = new UpdateUserPutBody();
        userPutBody.setTransactionLimit(BigDecimal.valueOf(-10));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1, userPutBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Cannot set transaction limit to a value less than 0.", exception.getReason());
    }

    @Test
    public void deleteUserById() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.of(expectedUsers.get(0)));

        // Execution
        userService.deleteUserById(1);
    }

    @Test
    public void deleteNonExistingUserByIdShouldThrowException() {
        // Setup
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // Execution
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.deleteUserById(1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals("Could not find an user with the given user ID.", exception.getReason());
    }

    private CreateUserPostBody getBaseCreateUserPostBody() {
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
    public void createUserById() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();

        User user = userService.createUser(createUserPostBody);

        // Assertions
        assertNotNull(user);
        assertEquals(createUserPostBody.getFirstName(), user.getFirstName());
        assertEquals(createUserPostBody.getLastName(), user.getLastName());
        assertEquals(createUserPostBody.getEmailAddress(), user.getEmailAddress());
        assertEquals(createUserPostBody.getRole(), user.getRole());
        assertEquals(createUserPostBody.getPhone(), user.getPhone());
        assertEquals(createUserPostBody.getTransactionLimit(), user.getTransactionLimit());
        assertEquals(createUserPostBody.getDayLimit(), user.getDayLimit());
        assertEquals(createUserPostBody.getBirthDate(), user.getBirthDate());
    }

    @Test
    public void createUserWithInvalidEmailAddressShouldThrowException() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.setEmailAddress("qewfqiuwef.com");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Invalid email address given.", exception.getReason());
    }

    @Test
    public void createUserWithInUseEmailAddressShouldThrowException() {
        // Setup
        given(userRepository.findByEmailAddress("alice@example.com")).willReturn(expectedUsers.get(0));

        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.setEmailAddress("alice@example.com");

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Email address is already in use.", exception.getReason());
    }

    @Test
    public void createUserWithoutRolesShouldThrowException() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.setRole(new ArrayList<>());

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("User should have at least one valid role.", exception.getReason());
    }

    @Test
    public void createUserWithInvalidRoleShouldThrowException() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.addRoleItem(null);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Roles field contains invalid value.", exception.getReason());
    }

    @Test
    public void createUserWithDuplicateRoleShouldThrowException() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.setRole(new ArrayList<>());
        createUserPostBody.addRoleItem(UserRole.EMPLOYEE);
        createUserPostBody.addRoleItem(UserRole.EMPLOYEE);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Roles field contains duplicate values.", exception.getReason());
    }

    @Test
    public void createUserWithValueOfDayLimitLessThanZeroShouldThrowException() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.setDayLimit(-3000f);

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Cannot set day limit to a value less than 0.", exception.getReason());
    }

    @Test
    public void createUserWithValueOfTransactionLimitLessThanZeroShouldThrowException() {
        // Execution
        CreateUserPostBody createUserPostBody = getBaseCreateUserPostBody();
        createUserPostBody.setTransactionLimit(BigDecimal.valueOf(-10));

        // Assertions
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(createUserPostBody));
        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals("Cannot set transaction limit to a value less than 0.", exception.getReason());
    }
}
package io.swagger.api;

import io.swagger.model.CreateUserPostBody;
import io.swagger.model.UpdateUserPutBody;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.UserRole;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")
@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<User>> usersGet() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    public ResponseEntity<User> usersPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody CreateUserPostBody body) {
        userService.createUser(body);
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    public ResponseEntity<Void> usersUserIdDelete(@Parameter(in = ParameterIn.PATH, description = "The ID of an user.", required=true, schema=@Schema()) @PathVariable("userId") Integer userId) throws NotFoundException {
        userService.deleteUserById(userId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<User> usersUserIdGet(@Parameter(in = ParameterIn.PATH, description = "The ID of an user.", required=true, schema=@Schema()) @PathVariable("userId") Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();

        User loggedInUser = userService.getUserByEmailAddress(emailAddress);
        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }

        if (!loggedInUser.getRole().contains(UserRole.EMPLOYEE) && loggedInUser.getId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
        }

        User user = userService.getUserById(userId);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    public ResponseEntity<Void> usersUserIdPut(@Parameter(in = ParameterIn.PATH, description = "The ID of an user.", required=true, schema=@Schema()) @PathVariable("userId") Integer userId,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UpdateUserPutBody body) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();

        // Check if the user is in the database
        User loggedInUser = userService.getUserByEmailAddress(emailAddress);
        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }

        if (!loggedInUser.getRole().contains(UserRole.EMPLOYEE)) {
            // Check if the user is trying to access another user
            if (loggedInUser.getId() != userId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
            }

            // There are some fields that a customer should not be able to change
            if (body.getDayLimit() != null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers cannot change their own day limit.");
            if (body.getTransactionLimit() != null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers cannot change their own transaction limit.");
            if (body.getRole() != null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers cannot change their own role.");
            if (body.getBirthDate() != null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers cannot change their own birthdate.");
            if (body.getFirstName() != null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers cannot change their firstname of their own.");
            if (body.getLastName() != null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customers cannot change their lastname of their own.");
        }

        userService.updateUserById(userId, body);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<User> usersSelfGet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAddress = authentication.getName();

        User loggedInUser = userService.getUserByEmailAddress(emailAddress);
        if (loggedInUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
        }

        return new ResponseEntity<User>(loggedInUser, HttpStatus.OK);
    }
}

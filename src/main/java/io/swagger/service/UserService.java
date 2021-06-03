package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.CreateUserPostBody;
import io.swagger.model.UpdateUserPutBody;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public String login(String emailaddress, String password) {
        try {
            //login
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailaddress, password));
            User user = userRepository.findByEmailAddress(emailaddress);
            return jwtTokenProvider.createToken(emailaddress, user.getRole());
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed, because of invalid input.");
        }
    }

    public User add(User user) {
        //Check of het al bestaat
        if (userRepository.findByEmailAddress(user.getEmailAddress()) == null) {
            Boolean validPass = false;
            if (user.getPassword() != null && user.getPassword().length() != 0)
            {
                validPass = true;
            }

            if (validPass) {
                user.setPassword(passwordEncoder.encode((user.getPassword())));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Password not valid");
            }
            return userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Emailaddress already being used.");
        }
    }

    //VALIDATE PASSWORD
    public static boolean isValidPassword(String password) {
        int charCounter = 0;
        if (password.length() >= 8) {
            for (int index = 0; index < password.length(); index++) {
                char passwordStringCharacter = password.charAt(index);
                if (!Character.isLetterOrDigit(passwordStringCharacter)) {
                    return false;
                } else {
                    if (Character.isDigit(passwordStringCharacter)) {
                        charCounter++;
                    }
                }
            }
        }
        if (charCounter < 2) {
            return false;
        }
        return true;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUserById(int id, UpdateUserPutBody body) {
        User user = this.getUserById(id);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an user with the given user ID.");
        }

        // Check if the email address is set and validate
        if (body.getEmailAddress() != null) {
            performEmailAddressValidation(body.getEmailAddress());
            user.setEmailAddress(body.getEmailAddress());
        }

        if (body.getDayLimit() != null) {
            if (body.getDayLimit() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot set day limit to a value less than 0.");
            }

            user.setDayLimit(body.getDayLimit());
        }

        if (body.getTransactionLimit() != null) {
            if (body.getTransactionLimit().longValueExact() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot set transaction limit to a value less than 0.");
            }

            user.setTransactionLimit(body.getTransactionLimit());
        }

        if (body.getRole() != null) {
            performRoleValidation(body.getRole());
            user.setRole(body.getRole());
        }

        // Check which fields are set in the request body and only change those fields
        if (body.getFirstName() != null) user.setFirstName(body.getFirstName());
        if (body.getLastName() != null) user.setFirstName(body.getLastName());
        if (body.getBirthDate() != null) user.setBirthDate(body.getBirthDate());
        if (body.getPhone() != null) user.setPhone(body.getPhone());
        if (body.getPassword() != null) user.setPassword(passwordEncoder.encode(body.getPassword()));

        userRepository.save(user);
        return user;
    }

    public User createUser(CreateUserPostBody body) {
        // Perform input validation
        performEmailAddressValidation(body.getEmailAddress());
        performRoleValidation(body.getRole());

        if (body.getDayLimit() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot set day limit to a value less than 0.");
        }

        if (body.getTransactionLimit().longValueExact() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot set transaction limit to a value less than 0.");
        }

        // Create a user object based of the body object
        User user = new User();
        user.firstName(body.getFirstName());
        user.lastName(body.getLastName());
        user.emailAddress(body.getEmailAddress());
        user.phone(body.getPhone());
        user.transactionLimit(body.getTransactionLimit());
        user.dayLimit(body.getDayLimit());
        user.birthDate(body.getBirthDate());
        user.password(body.getPassword()); // Password encoding is done in the 'add' method

        if (body.getRole() != null) {
            if (body.getRole().size() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User should have at least one role.");
            }

            if (body.getRole().size() > UserRole.values().length) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot have more than " + UserRole.values().length + " roles.");
            }

            user.role(body.getRole());
        }

        add(user);
        return user;
    }

    public void deleteUserById(int id) throws NotFoundException {
        User user = this.getUserById(id);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find an user with the given user ID.");
        }

        userRepository.delete(user);
    }

    public User getUserByEmailAddress(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

    private void performEmailAddressValidation(String emailAddress) {
        if (!checkEmailAddressFormat(emailAddress)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address given.");
        }

        User otherUser = getUserByEmailAddress(emailAddress);
        if (otherUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address is already in use.");
        }
    }

    private boolean checkEmailAddressFormat(String string) {
        String expression = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return Pattern.matches(expression, string);
    }

    private void performRoleValidation(List<UserRole> roles) {
        if (roles.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User should have at least one valid role.");
        }

        for (UserRole role : roles) {
            if (role == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Roles field contains invalid value.");
            }
        }

        List<UserRole> includedRoles = new ArrayList<>();
        for (UserRole role : roles) {
            if (includedRoles.contains(role)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Roles field contains duplicate values.");
            }

            includedRoles.add(role);
        }
    }
}


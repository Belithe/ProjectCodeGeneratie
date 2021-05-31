package io.swagger.service;

import io.swagger.api.NotFoundException;
import io.swagger.model.Body1;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Login failed.");
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

    public void updateUserById(int id, Body1 body) throws NotFoundException {
        User user = this.getUserById(id);

        if (user == null) {
            throw new NotFoundException(404, "Could not find an user with the given user ID.");
        }

        // TODO check authentication

        if (body.getEmailAddress() != null) user.setEmailAddress(body.getEmailAddress());
        if (body.getFirstName() != null) user.setFirstName(body.getFirstName());
        if (body.getLastName() != null) user.setFirstName(body.getLastName());
        if (body.getBirthDate() != null) user.setBirthDate(body.getBirthDate());
        if (body.getDayLimit() != null) user.setDayLimit(body.getDayLimit());
        if (body.getPhone() != null) user.setPhone(body.getPhone());

        userRepository.save(user);
    }

    public void deleteUserById(int id) throws NotFoundException {
        User user = this.getUserById(id);

        if (user == null) {
            throw new NotFoundException(404, "Could not find an user with the given user ID.");
        }

        userRepository.delete(user);
    }
}


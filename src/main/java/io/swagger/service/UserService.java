package io.swagger.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
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

    public String login(String username, String password) {
        try {
            //login
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUsername(username);
            return jwtTokenProvider.createToken(username, user.getRoles());
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Login failed.");
        }
    }

    public User add(User user) {
        //Check of het al bestaat
//        if (userRepository.findByUsername(user.getUsername()) == null) {
//            Boolean validPass = (user.getPassword());
//            if (validPass) {
//                user.setPassword(passwordEncoder.encode((user.getPassword())));
//            } else {
//                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Password not valid");
//            }
            return userRepository.save(user);
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already being used.");
//        }
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
}


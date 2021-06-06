package io.swagger.api;

import io.swagger.model.User;
import io.swagger.model.dto.ExceptionDTO;
import io.swagger.models.Response;
import io.swagger.repository.UserRepository;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseEntityExceptionManager extends ResponseEntityExceptionHandler {
    @Autowired
    UserService userService;

    @Order(1)
    @ExceptionHandler(value = { ResponseStatusException.class })
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        ExceptionDTO dto = new ExceptionDTO(ex.getStatus(), ex.getReason());
        return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), ex.getStatus(), request);
    }

    @Order(2)
    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleResponseAuthenticationException(BadCredentialsException ex, WebRequest request) {
        ExceptionDTO dto = new ExceptionDTO("Username/password invalid");
        return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Order(3)
    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> handleResponseAAException(Exception ex, WebRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && userService.getUserByEmailAddress(authentication.getName()) != null) {
            ExceptionDTO dto = new ExceptionDTO(HttpStatus.FORBIDDEN, "The current auth token does not provide access to this resource.");
            return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
        } else {
            ExceptionDTO dto = new ExceptionDTO(HttpStatus.UNAUTHORIZED, "No authentication token was given.");
            return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
        }
    }

    @Order(4)
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleResponseException(Exception ex, WebRequest request) {
        ExceptionDTO dto = new ExceptionDTO(ex.getMessage());
        return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
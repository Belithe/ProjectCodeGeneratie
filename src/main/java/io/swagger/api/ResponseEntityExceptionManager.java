package io.swagger.api;

import io.swagger.model.dto.ExceptionDTO;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseEntityExceptionManager extends ResponseEntityExceptionHandler {
    @Order(3)
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleResponseException(Exception ex, WebRequest request) {
        if (ex.getMessage().contains("Access is denied")) {
            ExceptionDTO dto = new ExceptionDTO(HttpStatus.UNAUTHORIZED,"Please login with a token.");
            return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
        } else {
            ExceptionDTO dto = new ExceptionDTO(ex.getMessage());
            return handleExceptionInternal(ex, dto.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }
    }

    @Order(1)
    @ExceptionHandler(value = {ResponseStatusException.class})
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
}
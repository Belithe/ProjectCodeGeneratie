package io.swagger.controller;

import io.swagger.api.NotFoundException;
import io.swagger.model.exception.Unauthorized;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseStatusExceptionResolver extends ResponseEntityExceptionHandler {
    @Order(1)
    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Order(2)
    @ExceptionHandler(value = { Unauthorized.class })
    protected ResponseEntity<Object> handleUnauthorized(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @Order(3)
    @ExceptionHandler(value = { ResponseStatusException.class })
    protected ResponseEntity<Object> handleUncaughtException(ResponseStatusException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getStatus(), request);
    }

    @Order(4)
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleUncaughtException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

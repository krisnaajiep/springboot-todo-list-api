package com.krisnaajiep.todolistapi.handler;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 06/05/25 00.16
@Last Modified 06/05/25 00.16
Version 1.0
*/

import com.krisnaajiep.todolistapi.exception.ForbiddenException;
import com.krisnaajiep.todolistapi.exception.TaskNotFoundException;
import com.krisnaajiep.todolistapi.exception.UnauthorizedException;
import lombok.NonNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final static String MESSAGE_KEY = "message";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(Map.of("errors", errors), headers, status);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKey(DuplicateKeyException ex) {
        String message = ex.getMostSpecificCause().getMessage();

        if (message.contains("UQ__User__A9D105342EA5682C")) {
            message = "Email already exists";
        }

        return new ResponseEntity<>(Map.of(MESSAGE_KEY, message), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleLoginException(UnauthorizedException ex) {
        return new ResponseEntity<>(
                Map.of(MESSAGE_KEY, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>(
                Map.of(MESSAGE_KEY, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        return new ResponseEntity<>(
                Map.of(MESSAGE_KEY, ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }
}

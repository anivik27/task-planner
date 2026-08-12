package org.vasya.task_planner.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class,
            IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> validateExceptionHandler(Exception e,
                                                                        HttpServletRequest request) {

        if (e instanceof HttpMessageNotReadableException ex) {
            log.error("Error on {}: {}", request.getRequestURI(), ex.getMessage());
            return ResponseEntity.status(400).body(Map.of("message", "incorrect value of field"));
        }

        if (e instanceof MethodArgumentNotValidException ex) {
            String message = ex.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse("Validation error");
            log.error("Error on {}: {}", request.getRequestURI(), ex.getMessage());
            return ResponseEntity.status(400).body(Map.of("message", message));
        }

        if (e instanceof IllegalArgumentException ex) {
            log.error("Error on {}: {}", request.getRequestURI(), e.getMessage());
            return ResponseEntity.status(400).body(Map.of("message", ex.getMessage()));
        }

        log.error("Error on {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotExist(Exception e,
                                                                HttpServletRequest request) {
        log.error("User not exist {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> userAlreadyExist(Exception e,
                                                                    HttpServletRequest request) {
        log.error("User already exist {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(409).body(Map.of("message", e.getMessage()));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> userNotAuthenticated(Exception e,
                                                                    HttpServletRequest request) {
        log.error("User is not authenticated {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> pathNotFound(Exception e,
                                                            HttpServletRequest request) {
        log.error("Resource not found {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> allExceptionHandler(Exception e,
                                                                   HttpServletRequest request) {
        log.error("Unhandled error on {}: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
    }
}
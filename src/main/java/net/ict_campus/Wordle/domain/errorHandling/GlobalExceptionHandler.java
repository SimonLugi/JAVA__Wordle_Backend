package net.ict_campus.Wordle.domain.errorHandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        //log error in orange
        LOGGER.warn("\u001B[33mElement not found: {}\u001B[0m", ex.getMessage());

        //return response to client
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Element not found: " + ex.getMessage());
    }

    /*
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        //log error in orange
        LOGGER.warn("\u001B[33mSomething went wrong (RuntimeException): {}\u001B[0m", ex.getMessage());

        //return response to client
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Something went wrong (RuntimeException): " + ex.getMessage());
    }
    */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();

        if (fieldError != null) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            Object rejectedValue = fieldError.getRejectedValue();

            //log error in orange
            LOGGER.warn("\u001B[33mValidation failed for field '{}': {} (rejected value: {})\u001B[0m",
                    field, defaultMessage, rejectedValue);

            //return message to client
            String userMessage = String.format("'%s': %s (you entered: %s)", field, defaultMessage, rejectedValue);
            return ResponseEntity
                    .badRequest()
                    .body(userMessage);
        }

        //return default message if FieldError is empty
        return ResponseEntity.badRequest().body("Validation failed with unknown field error");
    }
}

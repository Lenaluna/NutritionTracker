package com.example.NutritionTracker.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Global exception handler for the Nutrition Tracker application.
 * This class provides centralized exception handling across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors for @Valid request bodies.
     * Captures validation failures triggered by @Valid in DTOs
     * and returns a structured JSON response with field-specific error messages.
     *
     * Example response:
     * <pre>
     * {
     *   "name": "The name must be between 3 and 15 characters long",
     *   "weight": "The weight must be at least 1 kg",
     *   "age": "The age must not exceed 120 years"
     * }
     * </pre>
     *
     * Without this handler, Spring Boot would return a generic 500 Internal Server Error.
     *
     * @param ex the exception thrown due to validation failure
     * @return a map containing field-specific error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extracts validation errors and maps them to a readable response
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles invalid UUID format errors.
     * When an invalid UUID is passed as a parameter, a bad request response is returned.
     *
     * @param ex the exception triggered by an invalid UUID format
     * @return a response indicating the incorrect UUID format
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == UUID.class) {
            return ResponseEntity.badRequest().body("Invalid UUID format: " + ex.getValue());
        }
        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getValue());
    }

    /**
     * Handles cases where an entity is not found in the database.
     * Returns a 404 Not Found response when an entity lookup fails.
     *
     * @param ex the exception thrown when an entity is not found
     * @return a response with an appropriate error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles unexpected server errors and prevents raw stack traces from being exposed.
     * Returns a generic 500 Internal Server Error response.
     *
     * @param ex the unexpected exception
     * @return a response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }

    /**
     * Handles runtime exceptions.
     * Returns a generic error response to prevent exposing implementation details.
     *
     * @param ex the runtime exception
     * @return a response with a generic internal server error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred: " + ex.getMessage());
    }
}
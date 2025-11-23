package com.sharex.app.shared.web.advice;

import com.sharex.app.shared.web.dto.ApiError;
import com.sharex.app.shared.web.dto.ApiResponse;
import com.sharex.app.shared.web.dto.ValidationError;
import com.sharex.app.module.usergroup.domain.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------
    // 1) @Valid Validation Errors
    // -------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {

        List<ValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> ValidationError.of(
                        err.getField(),
                        err.getDefaultMessage(),
                        err.getRejectedValue()
                ))
                .sorted(Comparator.comparing(ValidationError::getField))
                .collect(Collectors.toList());

        ApiError apiError = ApiError.of(
                "Validation failed",
                "VALIDATION_ERROR",
                fieldErrors,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(ApiResponse.error(apiError));
    }

    // -------------------------
    // 2) For @Validated on path params / query params
    // -------------------------
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {

        List<ValidationError> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> ValidationError.of(
                        v.getPropertyPath().toString(),
                        v.getMessage(),
                        v.getInvalidValue()
                ))
                .collect(Collectors.toList());

        ApiError apiError = ApiError.of(
                "Validation failed",
                "CONSTRAINT_VIOLATION",
                errors,
                Instant.now()
        );

        return ResponseEntity.badRequest().body(ApiResponse.error(apiError));
    }

    // -------------------------
    // 3) Domain / Business Exceptions
    // -------------------------
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException ex) {
        ApiError err = ApiError.of(
                ex.getMessage(),
                "USER_NOT_FOUND",
                null,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(err));
    }

    // -------------------------
    // 4) Generic Fallback
    // -------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {

        ApiError apiError = ApiError.of(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                null,
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(apiError));
    }
}

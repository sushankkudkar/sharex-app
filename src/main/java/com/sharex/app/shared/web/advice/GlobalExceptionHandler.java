package com.sharex.app.shared.web.advice;

import com.sharex.app.shared.web.dto.*;
import com.sharex.app.shared.web.error.ErrorCode;
import com.sharex.app.shared.web.exception.*;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {

        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> ValidationError.of(
                        err.getField(),
                        err.getDefaultMessage(),
                        err.getRejectedValue()
                ))
                .sorted(Comparator.comparing(ValidationError::getField))
                .toList();

        ApiError apiError = ApiError.of(
                "Validation failed",
                ErrorCode.VALIDATION_ERROR.name(),
                errors,
                Instant.now()
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(apiError));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {

        List<ValidationError> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> ValidationError.of(
                        v.getPropertyPath().toString(),
                        v.getMessage(),
                        v.getInvalidValue()
                ))
                .toList();

        ApiError apiError = ApiError.of(
                "Validation failed",
                ErrorCode.CONSTRAINT_VIOLATION.name(),
                errors,
                Instant.now()
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(apiError));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {

        ApiError apiError = ApiError.of(
                ex.getMessage(),
                ex.getCode().name(),
                null,
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(apiError));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {

        ApiError apiError = ApiError.of(
                ex.getMessage(),
                ex.getCode().name(),
                null,
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(apiError));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ApiResponse<Void>> handleInfrastructure(InfrastructureException ex) {

        ApiError apiError = ApiError.of(
                "Service temporarily unavailable",
                ex.getCode().name(),
                null,
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.failure(apiError));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {

        ApiError apiError = ApiError.of(
                "Unexpected system error",
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                null,
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(apiError));
    }
}

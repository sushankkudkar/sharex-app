package com.sharex.app.shared.web.dto;

import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public final class ApiError {

    private final String message;
    private final String code;
    private final List<ValidationError> errors;
    private final Instant timestamp;

    private ApiError(String message, String code, List<ValidationError> errors, Instant timestamp) {
        this.message = message;
        this.code = code;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public static ApiError of(
            String message,
            String code,
            List<ValidationError> errors,
            Instant timestamp
    ) {
        return new ApiError(message, code, errors, timestamp);
    }

}

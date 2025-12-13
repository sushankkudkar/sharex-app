package com.sharex.app.shared.web.dto;

import lombok.Getter;

@Getter
public final class ValidationError {

    private final String field;
    private final String message;
    private final Object rejectedValue;

    private ValidationError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public static ValidationError of(String field, String message, Object rejectedValue) {
        return new ValidationError(field, message, rejectedValue);
    }

}

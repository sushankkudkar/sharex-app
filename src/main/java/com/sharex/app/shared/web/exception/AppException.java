package com.sharex.app.shared.web.exception;

import com.sharex.app.shared.web.error.ErrorCode;
import lombok.Getter;

@Getter
public abstract class AppException extends RuntimeException {

    private final ErrorCode code;

    protected AppException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }
}
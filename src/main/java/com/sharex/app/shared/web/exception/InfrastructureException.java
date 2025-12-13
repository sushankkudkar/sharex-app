package com.sharex.app.shared.web.exception;

import com.sharex.app.shared.web.error.ErrorCode;

public class InfrastructureException extends AppException {

    public InfrastructureException(String message, ErrorCode code, Throwable cause) {
        super(message, code);
        initCause(cause);
    }
}
package com.sharex.app.shared.web.exception;

import com.sharex.app.shared.web.error.ErrorCode;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String message, ErrorCode code) {
        super(message, code);
    }
}

package com.sharex.app.shared.web.exception;

import com.sharex.app.shared.web.error.ErrorCode;

public class BusinessException extends AppException {

    public BusinessException(String message, ErrorCode code) {
        super(message, code);
    }
}

package com.ql.ecommerce_backend.exceptions;

import lombok.Getter;
import lombok.Setter;

// Base exception class
@Getter
@Setter
public class BaseException extends RuntimeException {
    private final int errorCode;
    private final int httpStatus;

    public BaseException(String message, int errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

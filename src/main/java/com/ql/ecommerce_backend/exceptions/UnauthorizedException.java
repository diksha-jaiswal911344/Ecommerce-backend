package com.ql.ecommerce_backend.exceptions;

// Unauthorized exception
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(message, 401, 401);
    }
}

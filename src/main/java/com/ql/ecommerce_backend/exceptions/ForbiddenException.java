package com.ql.ecommerce_backend.exceptions;

// Forbidden exception
public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message, 403, 403);
    }
}

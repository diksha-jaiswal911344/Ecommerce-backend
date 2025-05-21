package com.ql.ecommerce_backend.exceptions;

// Bad request exception
public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message, 400, 400);
    }
}
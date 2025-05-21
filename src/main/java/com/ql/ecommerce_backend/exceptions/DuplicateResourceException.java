package com.ql.ecommerce_backend.exceptions;

// Duplicate resource exception
public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String message) {
        super(message, 409, 409);
    }
}

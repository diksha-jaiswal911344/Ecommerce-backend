package com.ql.ecommerce_backend.exceptions;

// Internal server error exception
public class InternalServerException extends BaseException {
    public InternalServerException(String message) {
        super(message, 500, 500);
    }
}

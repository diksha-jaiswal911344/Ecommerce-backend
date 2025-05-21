package com.ql.ecommerce_backend.exceptions;

public class OtpExpiredException extends RuntimeException{
    public OtpExpiredException(String message) {
        super(message);
    }
}

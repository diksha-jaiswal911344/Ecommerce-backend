package com.ql.ecommerce_backend.exceptions;

// OTP validation exception
public class OtpValidationException extends BaseException {
    public OtpValidationException(String message) {
        super(message, 400, 400);
    }
}
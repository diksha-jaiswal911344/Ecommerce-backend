package com.ql.ecommerce_backend.exceptions;

public class JwtAuthenticationException extends AuthenticationException{
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
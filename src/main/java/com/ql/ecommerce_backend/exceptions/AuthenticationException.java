package com.ql.ecommerce_backend.exceptions;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message){
        super(message);
    }
}

package com.ql.ecommerce_backend.exceptions;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException (String message){
        super(message);
    }
}

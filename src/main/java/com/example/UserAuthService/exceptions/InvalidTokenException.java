package com.example.UserAuthService.exceptions;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message){
        super(message);
    }
}

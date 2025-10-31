package com.example.UserAuthService.exceptions;

public class PasswordMissMatchException extends RuntimeException{
    public PasswordMissMatchException(String message){
        super(message);
    }
}

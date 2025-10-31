package com.example.UserAuthService.exceptions;

public class UserNotSignedException extends RuntimeException{

    public UserNotSignedException(String message){
        super(message);
    }
}

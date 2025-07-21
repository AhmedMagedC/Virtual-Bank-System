package com.microservice.user.service.exceptions;

public class InvalidUsernameOrPassword extends RuntimeException{

    public InvalidUsernameOrPassword(String message){
        super(message);
    }
}

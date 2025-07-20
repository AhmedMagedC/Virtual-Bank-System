package com.microservice.user_service.exceptions;

public class InvalidUsernameOrPassword extends RuntimeException{

    public InvalidUsernameOrPassword(String message){
        super(message);
    }
}

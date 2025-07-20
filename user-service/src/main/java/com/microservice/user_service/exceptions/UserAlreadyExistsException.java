package com.microservice.user_service.exceptions;

public class UserAlreadyExistsException extends  RuntimeException{

    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

package com.microservice.user.service.exceptions;

public class UserAlreadyExistsException extends  RuntimeException{

    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

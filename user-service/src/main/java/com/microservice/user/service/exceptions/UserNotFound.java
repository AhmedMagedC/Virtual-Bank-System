package com.microservice.user.service.exceptions;

public class UserNotFound extends RuntimeException{

    public UserNotFound(){}
    public UserNotFound(String message){
        super(message);
    }
}

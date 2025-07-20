package com.microservice.user_service.exceptions;

public class UserNotFound extends RuntimeException{

    public UserNotFound(){}
    public UserNotFound(String message){
        super(message);
    }
}

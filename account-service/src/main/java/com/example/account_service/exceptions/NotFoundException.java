package com.example.account_service.exceptions;


public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}

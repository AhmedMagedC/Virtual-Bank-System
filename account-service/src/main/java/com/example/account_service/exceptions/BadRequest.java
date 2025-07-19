package com.example.account_service.exceptions;

public class BadRequest extends RuntimeException {

    public BadRequest(String message) {
        super(message);
    }
}

package com.microservice.user.service.exceptions;


import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String message;
    private String error;

    public  ErrorResponse(){}

    public ErrorResponse(int status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }
}
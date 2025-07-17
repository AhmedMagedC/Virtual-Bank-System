package com.example.account_service.handlers;

import com.example.account_service.dtos.ErrorDetails;
import com.example.account_service.exceptions.AccountNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleAccountNotFound(AccountNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(404).body(errorDetails);
    }
}

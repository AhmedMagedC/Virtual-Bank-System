package com.example.account_service.handlers;

import com.example.account_service.dtos.ErrorDetails;
import com.example.account_service.exceptions.BadRequest;
import com.example.account_service.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.math.BigDecimal;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(NotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(404).body(errorDetails);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {

            String fieldName = "";
            if (!ife.getPath().isEmpty()) {
                fieldName = ife.getPath().get(0).getFieldName();
            }

            Class<?> targetType = ife.getTargetType();

            // Handle invalid enum
            if (targetType.isEnum()) {
                ErrorDetails errorDetails = new ErrorDetails(400,"Bad Request","Invalid value for enum field '" +
                        fieldName + "'. Allowed values: " +
                        Arrays.toString(targetType.getEnumConstants()));

                return ResponseEntity.badRequest()
                        .body(errorDetails);
            }

            // Handle invalid BigDecimal
            if (targetType == BigDecimal.class) {
                ErrorDetails errorDetails = new ErrorDetails(400,"Bad Request","Invalid numeric format for field '" +
                        fieldName +
                        "'. Expected a valid number.");

                return ResponseEntity.badRequest()
                        .body(errorDetails);
            }

            // Default fallback
            ErrorDetails errorDetails = new ErrorDetails(400,"Bad Request","Invalid value for field '" +
                    fieldName +
                    "': " + ife.getValue());

            return ResponseEntity.badRequest()
                    .body(errorDetails);
        }

        // Fallback for malformed JSON or unknown structure
        ErrorDetails errorDetails = new ErrorDetails(400,"Bad Request","Invalid request body: " + ex.getMessage());
        return ResponseEntity.badRequest()
                .body(errorDetails);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorDetails> handleBadRequest(BadRequest ex) {
        ErrorDetails errorDetails = new ErrorDetails(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(400).body(errorDetails);
    }
}

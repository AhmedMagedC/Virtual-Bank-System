package com.microservice.transaction.exceptions;

import com.microservice.transaction.enums.MsgType;
import com.microservice.transaction.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LoggingService loggingService;

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(BadRequestException ex) {
        ErrorResponse error = new  ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
                "Bad Request");
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(NotFoundException ex) {
        ErrorResponse error = new  ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                "Not Found");
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(), // or use a utility to format this better
                "Validation Failed"
        );

        loggingService.sendLog(errorResponse, MsgType.RESPONSE, LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleNpe(NullPointerException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Required field was null: " + ex.getMessage(),
                "Null Pointer"
        );
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "Bad Request"
        );
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }



}
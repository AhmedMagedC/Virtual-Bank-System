package com.microservice.user.service.exceptions;

import com.microservice.user.service.enums.MsgType;
import com.microservice.user.service.services.LoggingService;
import org.hibernate.PropertyValueException;
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

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleException(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(),
                "Conflict");
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error ;
    }

    @ExceptionHandler(value = InvalidUsernameOrPassword.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorResponse handleException(InvalidUsernameOrPassword ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),"Unauthorize");
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(value = UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(UserNotFound ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),"Not Found");
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        String combinedErrors = fieldErrors.toString(); // Optional: format as string
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                combinedErrors,
                "Validation Failed"
        );
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
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

    @ExceptionHandler(PropertyValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handlePropertyValue(PropertyValueException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "Bad Request"
        );
        loggingService.sendLog(error, MsgType.RESPONSE, LocalDateTime.now());
        return error;
    }
}

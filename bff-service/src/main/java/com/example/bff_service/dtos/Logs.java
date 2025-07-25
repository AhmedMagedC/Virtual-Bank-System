package com.example.bff_service.dtos;



import com.example.bff_service.enums.MsgType;
import java.time.LocalDateTime;


public class Logs {

    private String message;
    private MsgType messageType;
    private LocalDateTime dateTime;

    public Logs(){}

    public Logs(String message, MsgType messageType, LocalDateTime dateTime) {
        this.message = message;
        this.messageType = messageType;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MsgType getMessageType() {
        return messageType;
    }

    public void setMessageType(MsgType messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

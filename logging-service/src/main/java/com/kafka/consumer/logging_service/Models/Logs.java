package com.kafka.consumer.logging_service.Models;


import com.kafka.consumer.logging_service.enums.MsgType;
import jakarta.persistence.*;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MsgType messageType;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    public Logs(){}

    public Logs(String message, MsgType messageType, LocalDateTime dateTime) {
        this.message = message;
        this.messageType = messageType;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

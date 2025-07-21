package com.microservice.user.service.dtos;

import java.util.UUID;

public class UserResponse {
    private UUID userId;
    private String username;
    private String message;

    public UserResponse() {}

    public UserResponse(UUID userId, String username, String message) {
        this.userId = userId;
        this.username = username;
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

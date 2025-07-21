package com.microservice.user.service.dtos;

import java.util.UUID;

public class LoginResponse {
    private UUID userId;
    private String username;

    public LoginResponse() {}

    public LoginResponse(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
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
}


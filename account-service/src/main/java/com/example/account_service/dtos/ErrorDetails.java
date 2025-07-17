package com.example.account_service.dtos;

public record ErrorDetails(Integer status,
                           String error,
                           String message) { }

package com.example.account_service.dtos;

import java.util.UUID;

public record AccountCreationResponse(UUID accountId,
                                      String accountNumber,
                                      String message) { }

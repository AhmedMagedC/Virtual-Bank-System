package com.example.account_service.dtos;

import com.example.account_service.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountCreationRequest(UUID userId,
                                     AccountType accountType,
                                     BigDecimal initialBalance) { }

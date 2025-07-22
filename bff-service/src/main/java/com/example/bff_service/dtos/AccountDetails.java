package com.example.bff_service.dtos;

import com.example.bff_service.enums.AccountStatus;
import com.example.bff_service.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDetails(UUID accountId, String accountNumber,
                             BigDecimal balance,
                             AccountType accountType,
                             AccountStatus accountStatus) { }

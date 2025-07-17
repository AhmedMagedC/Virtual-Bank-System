package com.example.account_service.dtos;

import com.example.account_service.enums.AccountStatus;
import com.example.account_service.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDetails(UUID accountId, String accountNumber,
                             BigDecimal balance,
                             AccountType accountType,
                             AccountStatus accountStatus) { }

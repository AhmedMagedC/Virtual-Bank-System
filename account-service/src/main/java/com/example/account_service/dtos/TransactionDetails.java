package com.example.account_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDetails(
        UUID transactionId,
        UUID accountId,
        BigDecimal amount,
        String description,
        LocalDateTime timestamp
) {}

package com.example.account_service.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferExecutionRequest(UUID fromAccountId,
                                       UUID toAccountId,
                                       BigDecimal amount) { }

package com.microservice.transaction.dtos;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.UUID;

public class TransactionInternalRequest {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;

    public TransactionInternalRequest(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }
    public TransactionInternalRequest(){}

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(UUID fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(UUID toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

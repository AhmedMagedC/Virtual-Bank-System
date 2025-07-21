package com.microservice.transaction.dtos;

import jakarta.validation.constraints.NotNull;



import java.util.UUID;

public class TransferRequestExecution {
    @NotNull
    private UUID transactionId;

    public TransferRequestExecution(){}
    public TransferRequestExecution(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public @NotNull UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(@NotNull UUID transactionId) {
        this.transactionId = transactionId;
    }
}

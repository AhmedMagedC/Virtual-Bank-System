package com.microservice.transaction.dtos;

import com.microservice.transaction.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransferResponse {
    private UUID transactionId;
    private TransactionStatus status;
    private LocalDateTime timestamp;

    public TransferResponse(){}
    public TransferResponse(UUID transactionId, TransactionStatus status, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

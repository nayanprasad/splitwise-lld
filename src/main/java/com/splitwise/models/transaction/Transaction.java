package com.splitwise.models.transaction;

import com.splitwise.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class Transaction {
    protected UUID id;
    protected Float amount;
    protected LocalDateTime createdAt;
    protected  User createdBy;

    public Transaction(UUID id, Float amount, LocalDateTime date, User createdBy) {
        this.id = id;
        this.amount = amount;
        this.createdAt = date;
        this.createdBy = createdBy;
    }

    public abstract TransactionType getTransactionType();
}

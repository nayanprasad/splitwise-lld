package com.splitwise.models.transaction;

import com.splitwise.models.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SettlementTransaction extends Transaction {
    private User paidBy;
    private User paidTo;

    public SettlementTransaction(UUID id, Float amount, LocalDateTime date, User paidBy, User paidTo) {
        super(id, amount, date, paidBy);
        this.paidBy = paidBy;
        this.paidTo = paidTo;
    }

    public TransactionType getTransactionType() {
        return TransactionType.SETTLEMENT;
    }
}

package com.splitwise.models.transaction;

import com.splitwise.models.Group;
import com.splitwise.models.User;
import com.splitwise.models.split.Split;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ExpenseTransaction extends Transaction {
    private User paidBy;
    private Split split;
    private Group group;

    public ExpenseTransaction(UUID id, Float amount, LocalDateTime date, User paidBy, Split split, Group group) {
        super(id, amount, date, paidBy);
        this.paidBy = paidBy;
        this.split = split;
        this.group = group;
    }


    @Override
    public TransactionType getTransactionType() {
        return TransactionType.EXPENSE;
    }
}

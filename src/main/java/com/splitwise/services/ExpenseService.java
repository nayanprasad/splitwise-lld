package com.splitwise.services;

import com.splitwise.models.Group;
import com.splitwise.models.User;
import com.splitwise.models.split.EqualSplit;
import com.splitwise.models.split.Split;
import com.splitwise.models.transaction.ExpenseTransaction;
import com.splitwise.models.transaction.SettlementTransaction;
import com.splitwise.models.transaction.Transaction;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class ExpenseService {
    private static ExpenseService instance = null;
    private final List<Transaction> transactions;
    private final BalanceService balanceService;
    private final UserService userService;
    private final GroupService groupService;

    public static ExpenseService getInstance() {
        if(instance == null) {
            instance = new ExpenseService();
        }
        return instance;
    }

    public ExpenseService() {
        this.balanceService = BalanceService.getInstance();
        this.userService = UserService.getInstance();
        this.groupService = GroupService.getInstance();
        this.transactions = new ArrayList<>();
    }

    public ExpenseTransaction addEqualSplit(UUID id, Float amount, UUID paidById, UUID groupId, List<UUID> participantIds) {
        Group group = groupService.getGroup(groupId);
        User paidBy = userService.getUser(paidById);
        List<User> participants = new ArrayList<>();

        for(UUID userId : participantIds) {
            participants.add(userService.getUser(userId));
        }

        Split split = new EqualSplit(participants, amount);

        ExpenseTransaction expenseTransaction = new ExpenseTransaction(id, amount, LocalDateTime.now(), paidBy, split, group);

        updateBalancesFoExpence(expenseTransaction);
        transactions.add(expenseTransaction);
        return expenseTransaction;
    }

    public SettlementTransaction addSettlement(UUID id, Float amount, UUID paidById, UUID paidToId) {
        User paidBy = userService.getUser(paidById);
        User paidTo = userService.getUser(paidToId);

        Float balance = balanceService.getBalance(paidById, paidToId);

        if(balance < amount) {
            throw new IllegalArgumentException("settlement amount is Exceeded the balance");
        }

        balanceService.updateBalance(paidById, paidToId, amount);

        SettlementTransaction settlementTransaction = new SettlementTransaction(id, amount, LocalDateTime.now(), paidBy, paidTo);

        transactions.add(settlementTransaction);

        return settlementTransaction;
    }

    private void updateBalancesFoExpence(ExpenseTransaction  expenseTransaction) {
        User paidBy = expenseTransaction.getPaidBy();
        Map<User, Float> shares = expenseTransaction.getSplit().calculateSplitAmount();

        for(Map.Entry<User, Float> e: shares.entrySet()) {
            balanceService.updateBalance(e.getKey().getId(), paidBy.getId(), e.getValue());
        }
    }
}

package com.splitwise;

import com.splitwise.models.Group;
import com.splitwise.models.User;
import com.splitwise.models.transaction.ExpenseTransaction;
import com.splitwise.services.BalanceService;
import com.splitwise.services.ExpenseService;
import com.splitwise.services.GroupService;
import com.splitwise.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class App {
    private final UserService userService;
    private final GroupService groupService;
    private final BalanceService balanceService;
    private final ExpenseService expenseService;

    public App() {
        this.userService = UserService.getInstance();
        this.groupService = GroupService.getInstance();
        this.balanceService = BalanceService.getInstance();
        this.expenseService = ExpenseService.getInstance();
    }

    public void run() {
        System.out.println("=== Expense Sharing System Demo ===\n");

        User userA = userService.createUser(UUID.randomUUID(), "User A");
        User userB = userService.createUser(UUID.randomUUID(), "User B");
        User userC = userService.createUser(UUID.randomUUID(), "User C");

        balanceService.initializeUser(userA.getId());
        balanceService.initializeUser(userB.getId());
        balanceService.initializeUser(userC.getId());

        Group roomMate = new Group(UUID.randomUUID(), "Sahara hostel", userA);
        roomMate.addMember(userB);
        roomMate.addMember(userC);

        System.out.println(roomMate.getMembers());

        // Example 1: Equal expense
        System.out.println("\n=== Example 1: Equal Split ===");
        ExpenseTransaction expense1 = expenseService.addEqualSplit(
                UUID.randomUUID(),
                100.0f,
                userA.getId(),
                roomMate.getId(),
                List.of(userA.getId(), userB.getId(), userC.getId())
        );

        System.out.println("Added expense: " + expense1);
        showBalances();

    }

    private void showBalances() {
        System.out.println("\nCurrent balances:");
        Map<UUID, Map<UUID, Float>> allBalances = balanceService.getBalances();

        if (allBalances.isEmpty()) {
            System.out.println(" - No outstanding balances");
            return;
        }

        for (Map.Entry<UUID, Map<UUID, Float>> entry : allBalances.entrySet()) {
            UUID debtorId = entry.getKey();
            User debtor = userService.getUser(debtorId);

            for (Map.Entry<UUID, Float> balance : entry.getValue().entrySet()) {
                UUID creditorId = balance.getKey();
                User creditor = userService.getUser(creditorId);
                Float amount = balance.getValue();

                System.out.println(" - " + debtor.getName() + " owes " +
                        creditor.getName() + ": " + amount);
            }
        }
    }
}

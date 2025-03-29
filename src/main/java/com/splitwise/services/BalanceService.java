package com.splitwise.services;

import com.splitwise.models.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class BalanceService {
    private static BalanceService instance = null;
    private final Map<UUID, Map<UUID, Float>> balances;
    private final UserService userService;

    public BalanceService() {
        this.balances = new HashMap<>();
        this.userService = UserService.getInstance();
    }

    public static BalanceService getInstance() {
        if(instance == null) {
            instance = new BalanceService();
        }
        return instance;
    }

    public void initializeUser(UUID userId) {
        balances.put(userId, new HashMap<>());
    }

    public Map<User, Float> getUserBalances(UUID id) {
        User user = userService.getUser(id);

        Map<UUID, Float> balance = balances.get(id);
        Map<User, Float> userBalance = new HashMap<>();

        for (Map.Entry<UUID, Float> e : balance.entrySet()) {
            userBalance.put(userService.getUser(e.getKey()), e.getValue());
        }

        return userBalance;
    }

    public void updateBalance(UUID debtorId, UUID creditorId, Float amount) {
//        User debtor = userService.getUser(debtorId);
//        User creditor = userService.getUser(creditorId);

        Map<UUID, Float> debtorBalance = balances.get(debtorId);

        Float currentBalanceWithCreditor = debtorBalance.getOrDefault(creditorId, 0.0f);
        float newBalanceAmount = currentBalanceWithCreditor + amount;

        if(newBalanceAmount == 0) {
            debtorBalance.remove(creditorId);
        }
        else {
            debtorBalance.put(creditorId, newBalanceAmount);
        }
    }

    public Float getBalance(UUID debtorId, UUID creditorId) {
        Map<UUID, Float> debtorBalances = balances.get(debtorId);
        return debtorBalances.getOrDefault(creditorId, 0.0f);

    }

    public boolean clearBalance(UUID debtorId, UUID creditorId) {
        Map<UUID, Float> debtorBalance = balances.get(debtorId);
        debtorBalance.remove(creditorId);
        return true;
    }

}

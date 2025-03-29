package com.splitwise.models.split;

import com.splitwise.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExactSplit extends Split {

    private Map<User, Float> exactAmount;

    public ExactSplit(Map<User, Float> exactAmounts, Float totalAmount) {
        super(new ArrayList<>(exactAmounts.keySet()), totalAmount);
        this.exactAmount = new HashMap<>(exactAmounts);
    }

    @Override
    public Map<User, Float> calculateSplitAmount() {
        return exactAmount;
    }

    @Override
    public SplitType getSplitType() {
        return SplitType.EXACT;
    }
}

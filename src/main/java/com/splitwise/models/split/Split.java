package com.splitwise.models.split;

import com.splitwise.models.User;

import java.util.List;
import java.util.Map;

public abstract class Split {

    protected List<User> participants;
    protected Float totalAmount;

    public Split(List<User> participants, Float totalAmount) {
        this.participants = participants;
        this.totalAmount = totalAmount;
    }

    public abstract Map<User, Float> calculateSplitAmount();

    public abstract SplitType getSplitType();

}

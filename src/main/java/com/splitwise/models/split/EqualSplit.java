package com.splitwise.models.split;

import com.splitwise.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualSplit extends Split{
    public EqualSplit(List<User> participants, Float amount) {
        super(participants, amount);
        if(participants.isEmpty()) {
            throw new IllegalArgumentException("Empty participants");
        }
    }

    @Override
    public Map<User, Float> calculateSplitAmount() {
        Map<User, Float> shares = new HashMap<>();

        Float share = Math.round((totalAmount / participants.size()) * 100.0f) / 100.0f;

        for(User participant: participants) {
            shares.put(participant, share);
        }

        Float totalShare = share * participants.size();
        Float roundingError = totalAmount - totalShare;
        if(roundingError != 0) {
            User firstUser = participants.iterator().next();
            shares.put(firstUser, Math.round((shares.get(firstUser) + roundingError) * 100.0f) / 100.0f);
        }


        return shares;
    }

    @Override
    public SplitType getSplitType() {
        return SplitType.EQUAL;
    }
}

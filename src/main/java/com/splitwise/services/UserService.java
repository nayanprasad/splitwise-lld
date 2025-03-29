package com.splitwise.services;

import com.splitwise.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
    private static UserService instance = null;
    private final Map<UUID, User> users;

    public UserService() {
        this.users = new HashMap<>();
    }

    public static UserService getInstance() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User createUser(UUID id, String name) {
        User user = new User(id, name);
        users.put(id, user);
        return user;
    }

    public User getUser(UUID id) {
        return users.get(id);
    }
}

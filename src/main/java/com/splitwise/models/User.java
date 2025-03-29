package com.splitwise.models;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String name;

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName(String name) {
        return this.name;
    }





}

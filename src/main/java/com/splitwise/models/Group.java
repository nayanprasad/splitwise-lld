package com.splitwise.models;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class Group {
    private UUID id;
    private String name;
    private User creator;
    private Set<User> members;

    public Group(UUID id, String name, User creator, Set<User> members) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.members = members;
    }

    public Group(UUID id, String name, User creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.members = new HashSet<>();
    }

    public boolean addMember(User user) {
        return members.add(user);
    }

    public boolean removeMember(User user) {
        if(user.equals(creator)) return false;
        return members.remove(user);
    }

    public Set<User> getMembers() {
        return this.members;
    }



}

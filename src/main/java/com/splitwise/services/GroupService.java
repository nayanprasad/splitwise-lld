package com.splitwise.services;

import com.splitwise.models.Group;
import com.splitwise.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GroupService {
    private static GroupService instance = null;
    private final UserService userService;
    private final Map<UUID, Group> groups;


    public GroupService() {
        this.userService = UserService.getInstance();
        this.groups = new HashMap<>();
    }

    public static GroupService getInstance() {
        if(instance == null) {
            instance = new GroupService();
        }
        return instance;
    }

    public Group createGroup(UUID id, String name, User creator) {
        Group group = new Group(id, name, creator);
        groups.put(id, group);
        return group;
    }

    public Group getGroup(UUID id) {
        return groups.get(id);
    }

    public boolean addUserToGroup(UUID userId, UUID groupId) {
        User user = userService.getUser(userId);
        Group group = groups.get(groupId);
        return group.addMember(user);
    }

    public boolean removeUserFromGroup(UUID userId, UUID groupId) {
        User user = userService.getUser(userId);
        Group group = groups.get(groupId);
        return group.removeMember(user);
    }

    public Set<User> getGroupMembers(UUID groupId) {
        return groups.get(groupId).getMembers();
    }
}

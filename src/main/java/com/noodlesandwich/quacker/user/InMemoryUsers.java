package com.noodlesandwich.quacker.user;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUsers implements Users {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void register(String username) {
        users.put(username, new InMemoryUser());
    }

    @Override
    public User userNamed(String username) {
        if (!users.containsKey(username)) {
            throw new NonExistentUserException(username);
        }
        return users.get(username);
    }
}

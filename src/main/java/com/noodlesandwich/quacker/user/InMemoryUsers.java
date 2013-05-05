package com.noodlesandwich.quacker.user;

import java.util.HashMap;
import java.util.Map;
import com.google.inject.Inject;
import com.noodlesandwich.quacker.application.InMemory;

public class InMemoryUsers implements Users {
    private final Map<String, User> users = new HashMap<>();
    private final UserFactory userFactory;

    @Inject
    public InMemoryUsers(@InMemory UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public void register(String username) {
        users.put(username, userFactory.createUser());
    }

    @Override
    public User userNamed(String username) {
        if (!users.containsKey(username)) {
            throw new NonExistentUserException(username);
        }
        return users.get(username);
    }
}

package com.noodlesandwich.quacker.user;

import java.util.HashMap;
import java.util.Map;
import com.noodlesandwich.quacker.message.InMemoryTimeline;
import com.noodlesandwich.quacker.message.UpdatableTimeline;

public class InMemoryUsers implements Users {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Profile> profiles = new HashMap<>();

    @Override
    public void register(String username) {
        UpdatableTimeline timeline = new InMemoryTimeline();
        users.put(username, new InMemoryUser(timeline));
        profiles.put(username, new InMemoryProfile());
    }

    @Override
    public User userNamed(String username) {
        if (!users.containsKey(username)) {
            throw new NonExistentUserException(username);
        }
        return users.get(username);
    }

    @Override
    public Profile profileFor(String username) {
        return profiles.get(username);
    }
}

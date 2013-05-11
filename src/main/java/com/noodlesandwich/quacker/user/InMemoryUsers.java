package com.noodlesandwich.quacker.user;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.message.Feed;
import com.noodlesandwich.quacker.message.InMemoryFeed;
import com.noodlesandwich.quacker.message.InMemoryTimeline;
import com.noodlesandwich.quacker.message.UpdatableTimeline;

@Singleton
public class InMemoryUsers implements Users, Profiles {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Profile> profiles = new HashMap<>();

    @Override
    public void register(String username) {
        UpdatableTimeline timeline = new InMemoryTimeline();
        Feed feed = new InMemoryFeed();
        users.put(username, new InMemoryUser(timeline, feed));
        profiles.put(username, new InMemoryProfile(timeline));
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
        if (!profiles.containsKey(username)) {
            throw new NonExistentUserException(username);
        }
        return profiles.get(username);
    }
}

package com.noodlesandwich.quacker.user;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.message.AggregatedProfileFeed;
import com.noodlesandwich.quacker.message.Feed;
import com.noodlesandwich.quacker.message.InMemoryTimeline;
import com.noodlesandwich.quacker.message.UpdatableTimeline;

@Singleton
public class InMemoryUsers implements Users, Profiles {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Profile> profiles = new HashMap<>();

    @Override
    public void register(String username) {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException(username);
        }

        UpdatableTimeline timeline = new InMemoryTimeline();
        InMemoryProfile profile = new InMemoryProfile(timeline);
        Feed feed = new AggregatedProfileFeed(profile);
        InMemoryUser user = new InMemoryUser(username, timeline, feed);

        users.put(username, user);
        profiles.put(username, profile);
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

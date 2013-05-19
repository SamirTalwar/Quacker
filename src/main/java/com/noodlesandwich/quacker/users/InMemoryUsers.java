package com.noodlesandwich.quacker.users;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import com.google.inject.Inject;
import com.noodlesandwich.quacker.communication.feed.AggregatedProfileFeed;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.CompositeMessageListener;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.timeline.InMemoryTimeline;

@Singleton
public class InMemoryUsers implements Users, Profiles {
    private final MessageListener messageListener;

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Profile> profiles = new HashMap<>();

    @Inject
    public InMemoryUsers(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void register(String username) {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException(username);
        }

        InMemoryTimeline timeline = new InMemoryTimeline();
        InMemoryProfile profile = new InMemoryProfile(timeline);
        Feed feed = new AggregatedProfileFeed(profile);
        MessageListener messageListenerWithTimeline = new CompositeMessageListener(timeline, messageListener);
        DelegatingUser user = new DelegatingUser(username, timeline, feed, messageListenerWithTimeline);

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

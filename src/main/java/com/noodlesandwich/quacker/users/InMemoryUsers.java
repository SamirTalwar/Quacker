package com.noodlesandwich.quacker.users;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import com.google.inject.Inject;
import com.noodlesandwich.quacker.communication.feed.AggregatedProfileFeed;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.timeline.InMemoryTimeline;
import com.noodlesandwich.quacker.id.IdentifierSource;

@Singleton
public class InMemoryUsers implements Users, Profiles {
    private final Clock clock;
    private final IdentifierSource idSource;
    private final Collection<MessageListener> messageListeners;

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Profile> profiles = new HashMap<>();

    @Inject
    public InMemoryUsers(Clock clock, IdentifierSource idSource, Set<MessageListener> messageListeners) {
        this.clock = clock;
        this.idSource = idSource;
        this.messageListeners = messageListeners;
    }

    @Override
    public void register(String username) {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException(username);
        }

        InMemoryTimeline timeline = new InMemoryTimeline();
        InMemoryProfile profile = new InMemoryProfile(timeline);
        Feed feed = new AggregatedProfileFeed(profile);
        List<MessageListener> messageListenersWithTimeline = new ArrayList<>(messageListeners);
        messageListenersWithTimeline.add(timeline);
        DelegatingUser user = new DelegatingUser(clock, idSource, username, timeline, feed, messageListenersWithTimeline);

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

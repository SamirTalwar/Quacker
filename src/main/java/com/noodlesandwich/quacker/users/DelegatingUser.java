package com.noodlesandwich.quacker.users;

import java.time.Clock;
import java.util.Set;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.timeline.Timeline;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.id.IdentifierSource;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public class DelegatingUser implements User {
    private final Clock clock;
    private final IdentifierSource idSource;
    private final String username;
    private final Timeline timeline;
    private final Feed feed;
    private final Set<MessageListener> messageListeners;

    public DelegatingUser(Clock clock, IdentifierSource idSource, String username, Timeline timeline, Feed feed, Set<MessageListener> messageListeners) {
        this.clock = clock;
        this.idSource = idSource;
        this.username = username;
        this.timeline = timeline;
        this.feed = feed;
        this.messageListeners = messageListeners;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void publish(String message) {
        Id messageId = idSource.nextId();
        for (MessageListener messageListener : messageListeners) {
            messageListener.publish(messageId, this, message, clock.instant());
        }
    }

    @Override
    public void follow(Profile profile) {
        feed.follow(profile);
    }

    @Override
    public void renderTimelineTo(TimelineRenderer renderer) {
        timeline.renderTo(renderer);
    }

    @Override
    public void renderFeedTo(FeedRenderer renderer) {
        feed.renderTo(renderer);
    }
}

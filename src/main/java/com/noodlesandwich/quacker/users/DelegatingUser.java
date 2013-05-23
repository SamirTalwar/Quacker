package com.noodlesandwich.quacker.users;

import java.time.Clock;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.Message;
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
    private final MessageListener messageListener;

    public DelegatingUser(Clock clock, IdentifierSource idSource, String username, Timeline timeline, Feed feed, MessageListener messageListener) {
        this.clock = clock;
        this.idSource = idSource;
        this.username = username;
        this.timeline = timeline;
        this.feed = feed;
        this.messageListener = messageListener;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void publish(String message) {
        Id messageId = idSource.nextId();
        messageListener.publish(messageId, new Message(messageId, this, message, clock.instant()));
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

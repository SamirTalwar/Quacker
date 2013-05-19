package com.noodlesandwich.quacker.users;

import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.timeline.Timeline;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public class DelegatingUser implements User {
    private final String username;
    private final Timeline timeline;
    private final Feed feed;
    private final MessageListener messageListener;

    public DelegatingUser(String username, Timeline timeline, Feed feed, MessageListener messageListener) {
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
    public void publish(Id messageId, Message message) {
        messageListener.publish(messageId, message);
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

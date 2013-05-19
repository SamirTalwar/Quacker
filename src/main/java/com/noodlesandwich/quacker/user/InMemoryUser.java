package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Feed;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.UpdatableTimeline;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public class InMemoryUser implements User {
    private final String username;
    private final UpdatableTimeline timeline;
    private final Feed feed;

    public InMemoryUser(String username, UpdatableTimeline timeline, Feed feed) {
        this.username = username;
        this.timeline = timeline;
        this.feed = feed;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void publish(Message message) {
        timeline.publish(message);
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

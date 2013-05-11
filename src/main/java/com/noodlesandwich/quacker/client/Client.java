package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface Client {
    void publish(String message);
    void follow(String followee);

    void openTimelineOf(String username, TimelineRenderer renderer);
    void openFeed(FeedRenderer renderer);
}

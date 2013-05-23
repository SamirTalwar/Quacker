package com.noodlesandwich.quacker.users;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface User {
    String getUsername();

    void publish(String message);

    void follow(Profile profile);

    void renderTimelineTo(TimelineRenderer renderer);

    void renderFeedTo(FeedRenderer renderer);
}

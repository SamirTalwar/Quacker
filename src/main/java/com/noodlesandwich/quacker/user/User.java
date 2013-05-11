package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface User {
    void publish(Message message);

    void follow(Profile profile);

    void renderTimelineTo(TimelineRenderer renderer);

    void renderFeedTo(FeedRenderer renderer);
}

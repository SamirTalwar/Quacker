package com.noodlesandwich.quacker.communication.feed;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.users.Profile;

public interface Feed {
    int MAXIMUM_FEED_LENGTH = 20;

    void follow(Profile profile);

    void renderTo(FeedRenderer renderer);
}

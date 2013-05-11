package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.user.Profile;

public interface Feed {
    int MAXIMUM_FEED_LENGTH = 20;

    void follow(Profile profile);

    void renderTo(FeedRenderer renderer);
}

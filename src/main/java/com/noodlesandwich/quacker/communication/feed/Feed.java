package com.noodlesandwich.quacker.communication.feed;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.users.Profile;

public interface Feed {
    int MaximumFeedLength = 20;

    void follow(Profile profile);

    void block(String string);

    void renderTo(FeedRenderer renderer);
}

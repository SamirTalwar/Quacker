package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.user.Profile;

public class InMemoryFeed implements Feed {
    private Profile followee;

    @Override
    public void follow(Profile profile) {
        this.followee = profile;
    }

    @Override
    public void renderTo(FeedRenderer renderer) {
        if (followee == null) {
            return;
        }

        followee.renderTimelineTo(new TimelineRenderer() {
            @Override
            public void render(Message message) {
                renderer.render(message);
            }
        });
    }
}

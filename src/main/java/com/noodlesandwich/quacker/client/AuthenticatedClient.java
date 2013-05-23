package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.communication.conversations.Conversations;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.ConversationRenderer;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.users.Profiles;
import com.noodlesandwich.quacker.users.User;

public class AuthenticatedClient implements Client {
    private final User user;
    private final Profiles profiles;
    private final Conversations conversations;

    public AuthenticatedClient(User user, Profiles profiles, Conversations conversations) {
        this.user = user;
        this.profiles = profiles;
        this.conversations = conversations;
    }

    @Override
    public void publish(String message) {
        user.publish(message);
    }

    @Override
    public void follow(String followee) {
        user.follow(profiles.profileFor(followee));
    }

    @Override
    public void openTimelineOf(String username, TimelineRenderer renderer) {
        profiles.profileFor(username).renderTimelineTo(renderer);
    }

    @Override
    public void openFeed(FeedRenderer renderer) {
        user.renderFeedTo(renderer);
    }

    @Override
    public void viewConversationAround(Id messageId, ConversationRenderer renderer) {
        conversations.conversationAround(messageId).renderConversationTo(renderer);
    }
}

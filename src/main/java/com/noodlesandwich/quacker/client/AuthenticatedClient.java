package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.UserInterface;
import com.noodlesandwich.quacker.user.Profiles;
import com.noodlesandwich.quacker.user.User;

public class AuthenticatedClient implements Client {
    private final UserInterface userInterface;
    private final User user;
    private final Profiles profiles;

    public AuthenticatedClient(UserInterface userInterface, User user, Profiles profiles) {
        this.userInterface = userInterface;
        this.user = user;
        this.profiles = profiles;
    }

    @Override
    public void publish(String message) {
        user.publish(new Message(message));
    }

    @Override
    public void follow(String followee) {
        user.follow(profiles.profileFor(followee));
    }

    @Override
    public void openTimelineOf(String username) {
        profiles.profileFor(username).renderTimelineTo(userInterface);
    }

    @Override
    public void openFeed() {
        user.renderFeedTo(userInterface);
    }
}

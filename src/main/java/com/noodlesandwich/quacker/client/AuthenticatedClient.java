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
    public void openTimelineOf(String username) {
        profiles.forUser(username).renderTimelineTo(userInterface);
    }
}

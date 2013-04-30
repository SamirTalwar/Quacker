package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.Timelines;
import com.noodlesandwich.quacker.ui.UserInterface;
import com.noodlesandwich.quacker.user.User;

public class AuthenticatedClient implements Client {
    private final UserInterface userInterface;
    private final User user;
    private final Timelines timelines;

    public AuthenticatedClient(UserInterface userInterface, User user, Timelines timelines) {
        this.userInterface = userInterface;
        this.user = user;
        this.timelines = timelines;
    }

    @Override
    public void publish(String message) {
        user.publish(new Message(message));
    }

    @Override
    public void openTimelineOf(String username) {
        timelines.forUser(username).renderTo(userInterface);
    }
}

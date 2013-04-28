package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.user.User;

public class AuthenticatedClient implements Client {
    private final User user;

    public AuthenticatedClient(User user) {
        this.user = user;
    }

    @Override
    public void publish(String message) {
        user.publish(new Message(message));
    }

    @Override
    public void openTimelineOf(String user) {
        throw new UnsupportedOperationException();
    }
}

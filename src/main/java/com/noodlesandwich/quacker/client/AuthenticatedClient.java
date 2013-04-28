package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.user.User;

public class AuthenticatedClient implements Client {
    public AuthenticatedClient(User user) {
    }

    @Override
    public void publish(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void openTimelineOf(String user) {
        throw new UnsupportedOperationException();
    }
}

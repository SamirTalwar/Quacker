package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.User;

public class ApplicationServer implements Server {
    @Override
    public User authenticatedUserNamed(String username) {
        throw new UnsupportedOperationException();
    }
}

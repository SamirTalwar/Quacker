package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.User;
import com.noodlesandwich.quacker.user.Users;

public class ApplicationServer implements Server {
    private final Users users;

    public ApplicationServer(Users users) {
        this.users = users;
    }

    @Override
    public User authenticatedUserNamed(String username) {
        return users.userNamed(username);
    }
}

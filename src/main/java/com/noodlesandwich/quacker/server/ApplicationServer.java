package com.noodlesandwich.quacker.server;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.user.User;
import com.noodlesandwich.quacker.user.Users;

@Singleton
public class ApplicationServer implements Server {
    private final Users users;

    @Inject
    public ApplicationServer(Users users) {
        this.users = users;
    }

    @Override
    public void registerUserNamed(String username) {
        users.register(username);
    }

    @Override
    public User authenticatedUserNamed(String username) {
        return users.userNamed(username);
    }
}

package com.noodlesandwich.quacker.server;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.user.Profile;
import com.noodlesandwich.quacker.user.Profiles;
import com.noodlesandwich.quacker.user.User;
import com.noodlesandwich.quacker.user.Users;

@Singleton
public class ApplicationServer implements Server {
    private final Users users;
    private final Profiles profiles;

    @Inject
    public ApplicationServer(Users users, Profiles profiles) {
        this.users = users;
        this.profiles = profiles;
    }

    @Override
    public void registerUserNamed(String username) {
        users.register(username);
    }

    @Override
    public User authenticatedUserNamed(String username) {
        return users.userNamed(username);
    }

    @Override
    public Profile profileFor(String username) {
        return profiles.profileFor(username);
    }
}

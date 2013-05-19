package com.noodlesandwich.quacker.server;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.Profiles;
import com.noodlesandwich.quacker.users.User;
import com.noodlesandwich.quacker.users.Users;

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

    @Override
    public Conversation conversationAround(Id messageId) {
        throw new UnsupportedOperationException();
    }
}

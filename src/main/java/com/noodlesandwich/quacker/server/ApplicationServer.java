package com.noodlesandwich.quacker.server;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.communication.conversations.Conversations;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.Profiles;
import com.noodlesandwich.quacker.users.User;
import com.noodlesandwich.quacker.users.Users;

@Singleton
public class ApplicationServer implements Server {
    private final Users users;
    private final Profiles profiles;
    private final Conversations conversations;

    @Inject
    public ApplicationServer(Users users, Profiles profiles, Conversations conversations) {
        this.users = users;
        this.profiles = profiles;
        this.conversations = conversations;
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
        return conversations.conversationAround(messageId);
    }
}

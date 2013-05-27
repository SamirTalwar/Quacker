package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.User;

public interface Server extends RemoteServer {
    @Override
    void registerUserNamed(String username);

    @Override
    User authenticatedUserNamed(String username);

    @Override
    Profile profileFor(String username);

    @Override
    Conversation conversationAround(Id messageId);
}

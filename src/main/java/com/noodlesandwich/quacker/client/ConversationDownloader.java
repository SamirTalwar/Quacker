package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.communication.conversations.Conversations;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.server.Server;

public class ConversationDownloader implements Conversations {
    private final Server server;

    public ConversationDownloader(Server server) {
        this.server = server;
    }

    @Override
    public Conversation conversationAround(Id messageId) {
        return server.conversationAround(messageId);
    }
}

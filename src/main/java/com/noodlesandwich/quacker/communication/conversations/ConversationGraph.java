package com.noodlesandwich.quacker.communication.conversations;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import com.google.inject.Singleton;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.messages.NonExistentMessageException;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.User;

@Singleton
public class ConversationGraph implements Conversations, MessageListener {
    private final Map<Id, Message> messages = new HashMap<>();

    @Override
    public void publish(Id id, User author, String text, Instant timestamp) {
        messages.put(id, new Message(id, author, text, timestamp));
    }

    @Override
    public Conversation conversationAround(Id messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NonExistentMessageException(messageId);
        }
        return new SortedConversation(messages.get(messageId));
    }
}

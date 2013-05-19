package com.noodlesandwich.quacker.communication.conversations;

import java.util.HashMap;
import java.util.Map;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.id.Id;

public class ConversationGraph implements Conversations, MessageListener {
    private final Map<Id, Message> messages = new HashMap<>();

    @Override
    public void register(Id id, Message message) {
        messages.put(id, message);
    }

    @Override
    public Conversation conversationAround(Id messageId) {
        return new SortedConversation(messages.get(messageId));
    }
}

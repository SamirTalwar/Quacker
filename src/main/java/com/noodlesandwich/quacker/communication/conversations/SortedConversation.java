package com.noodlesandwich.quacker.communication.conversations;

import java.util.Arrays;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.ConversationRenderer;

public class SortedConversation implements Conversation {
    private final Iterable<Message> messages;

    public SortedConversation(Message... messages) {
        this(Arrays.asList(messages));
    }

    public SortedConversation(Iterable<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void renderConversationTo(ConversationRenderer renderer) {
       for (Message message : messages) {
           renderer.render(message);
       }
    }
}

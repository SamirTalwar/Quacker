package com.noodlesandwich.quacker.communication.conversations;

import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.ConversationRenderer;

public class SortedConversation implements Conversation {
    private final SortedSet<Message> messages;

    public SortedConversation(Message... messages) {
        this(Arrays.asList(messages));
    }

    public SortedConversation(Collection<Message> messages) {
        this.messages = new TreeSet<>(messages);
    }

    @Override
    public void renderConversationTo(ConversationRenderer renderer) {
       for (Message message : messages) {
           renderer.render(message);
       }
    }
}

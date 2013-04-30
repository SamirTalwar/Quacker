package com.noodlesandwich.quacker.user;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.MessageRenderer;

public class InMemoryUser implements User {
    private List<Message> messages = new ArrayList<>();

    @Override
    public void publish(Message message) {
        this.messages.add(message);
    }

    @Override
    public void renderTimelineTo(MessageRenderer renderer) {
        ListIterator<Message> messageIterator = messages.listIterator(messages.size());
        while (messageIterator.hasPrevious()) {
            messageIterator.previous().renderTo(renderer);
        }
    }
}

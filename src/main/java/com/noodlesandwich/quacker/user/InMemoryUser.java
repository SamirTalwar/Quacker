package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.MessageRenderer;

public class InMemoryUser implements User {
    private Message message;

    @Override
    public void publish(Message message) {
        this.message = message;
    }

    @Override
    public void renderTimelineTo(MessageRenderer renderer) {
        message.renderTo(renderer);
    }
}

package com.noodlesandwich.quacker.ui;

import com.noodlesandwich.quacker.communication.messages.Message;

public class MessageListRenderer implements ConversationRenderer, FeedRenderer, TimelineRenderer {
    private final MessageRenderer messageRenderer;

    public MessageListRenderer(MessageRenderer messageRenderer) {
        this.messageRenderer = messageRenderer;
    }

    @Override
    public void render(Message message) {
        message.renderTo(messageRenderer);
    }
}

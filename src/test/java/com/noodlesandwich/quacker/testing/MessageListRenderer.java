package com.noodlesandwich.quacker.testing;

import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.ConversationRenderer;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

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

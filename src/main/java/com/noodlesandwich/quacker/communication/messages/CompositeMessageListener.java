package com.noodlesandwich.quacker.communication.messages;

import java.time.Instant;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.User;

public class CompositeMessageListener implements MessageListener {
    private final MessageListener[] messageListeners;

    public CompositeMessageListener(MessageListener... messageListeners) {
        this.messageListeners = messageListeners;
    }

    @Override
    public void publish(Id id, User author, String text, Instant timestamp) {
        for (MessageListener messageListener : messageListeners) {
            messageListener.publish(id, author, text, timestamp);
        }
    }
}

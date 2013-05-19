package com.noodlesandwich.quacker.communication.messages;

import com.noodlesandwich.quacker.id.Id;

public class CompositeMessageListener implements MessageListener {
    private final MessageListener[] messageListeners;

    public CompositeMessageListener(MessageListener... messageListeners) {
        this.messageListeners = messageListeners;
    }

    @Override
    public void publish(Id id, Message message) {
        for (MessageListener messageListener : messageListeners) {
            messageListener.publish(id, message);
        }
    }
}

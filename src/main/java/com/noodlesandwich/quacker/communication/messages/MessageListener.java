package com.noodlesandwich.quacker.communication.messages;

import com.noodlesandwich.quacker.id.Id;

public interface MessageListener {
    void publish(Id id, Message message);
}

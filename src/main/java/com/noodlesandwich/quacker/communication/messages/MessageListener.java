package com.noodlesandwich.quacker.communication.messages;

import com.noodlesandwich.quacker.id.Id;

public interface MessageListener {
    void register(Id id, Message message);
}

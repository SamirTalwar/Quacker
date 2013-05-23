package com.noodlesandwich.quacker.communication.messages;

import java.time.Instant;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.User;

public interface MessageListener {
    void publish(Id id, User author, String text, Instant timestamp);
}

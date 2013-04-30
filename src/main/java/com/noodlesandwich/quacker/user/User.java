package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.MessageRenderer;

public interface User {
    void publish(Message message);

    void renderTimelineTo(MessageRenderer renderer);
}

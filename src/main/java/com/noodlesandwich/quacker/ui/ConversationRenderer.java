package com.noodlesandwich.quacker.ui;

import com.noodlesandwich.quacker.communication.messages.Message;

public interface ConversationRenderer {
    void render(Message message);
}

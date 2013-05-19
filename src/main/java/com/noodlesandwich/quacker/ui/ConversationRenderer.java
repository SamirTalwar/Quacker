package com.noodlesandwich.quacker.ui;

import com.noodlesandwich.quacker.message.Message;

public interface ConversationRenderer {
    void render(Message message);
}

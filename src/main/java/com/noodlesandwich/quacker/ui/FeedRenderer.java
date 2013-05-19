package com.noodlesandwich.quacker.ui;

import com.noodlesandwich.quacker.communication.messages.Message;

public interface FeedRenderer {
    void render(Message message);
}

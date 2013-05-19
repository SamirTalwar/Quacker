package com.noodlesandwich.quacker.ui;

import com.noodlesandwich.quacker.communication.messages.Message;

public interface TimelineRenderer {
    void render(Message message);
}

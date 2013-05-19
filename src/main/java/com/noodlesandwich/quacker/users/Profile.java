package com.noodlesandwich.quacker.users;

import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface Profile extends Iterable<Message> {
    void renderTimelineTo(TimelineRenderer renderer);
}

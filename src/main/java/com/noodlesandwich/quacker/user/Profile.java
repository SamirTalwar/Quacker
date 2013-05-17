package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface Profile extends Iterable<Message> {
    void renderTimelineTo(TimelineRenderer renderer);
}

package com.noodlesandwich.quacker.communication.timeline;

import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface Timeline extends Iterable<Message> {
    void renderTo(TimelineRenderer renderer);
}

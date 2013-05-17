package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface Timeline extends Iterable<Message> {
    void renderTo(TimelineRenderer renderer);
}

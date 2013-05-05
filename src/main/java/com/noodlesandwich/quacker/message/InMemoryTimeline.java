package com.noodlesandwich.quacker.message;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public class InMemoryTimeline implements UpdatableTimeline {
    private final List<Message> messages = new ArrayList<>();

    @Override
    public void publish(Message message) {
        messages.add(message);
    }

    @Override
    public void renderTo(TimelineRenderer renderer) {
        ListIterator<Message> messageIterator = messages.listIterator(messages.size());
        while (messageIterator.hasPrevious()) {
            renderer.render(messageIterator.previous());
        }
    }
}

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
        int count = 0;
        ListIterator<Message> messageIterator = messages.listIterator(messages.size());
        while (messageIterator.hasPrevious()) {
            if (count == Feed.MAXIMUM_FEED_LENGTH) {
                break;
            }
            renderer.render(messageIterator.previous());
            count++;
        }
    }
}

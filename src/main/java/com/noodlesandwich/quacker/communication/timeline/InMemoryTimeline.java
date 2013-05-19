package com.noodlesandwich.quacker.communication.timeline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public class InMemoryTimeline implements UpdatableTimeline {
    private final List<Message> messages = new LinkedList<>();

    @Override
    public void publish(Message message) {
        messages.add(0, message);
    }

    @Override
    public void renderTo(TimelineRenderer renderer) {
        int count = 0;
        for (Message message : messages) {
            if (count == Feed.MAXIMUM_FEED_LENGTH) {
                break;
            }
            renderer.render(message);
            count++;
        }
    }

    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }
}
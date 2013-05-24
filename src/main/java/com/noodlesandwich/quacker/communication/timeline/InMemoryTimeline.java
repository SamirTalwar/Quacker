package com.noodlesandwich.quacker.communication.timeline;

import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.users.User;

public class InMemoryTimeline implements Timeline, MessageListener {
    private final List<Message> messages = new LinkedList<>();

    @Override
    public void publish(Id id, User author, String text, Instant timestamp) {
        messages.add(0, new Message(id, author, text, timestamp));
    }

    @Override
    public void renderTo(TimelineRenderer renderer) {
        int count = 0;
        for (Message message : messages) {
            if (count == Feed.MaximumFeedLength) {
                break;
            }
            renderer.render(message);
            count++;
        }
    }

    @Override
    public Iterator<Message> iterator() {
        return Collections.unmodifiableList(messages).iterator();
    }
}

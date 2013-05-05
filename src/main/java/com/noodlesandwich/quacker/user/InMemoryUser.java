package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.UpdatableTimeline;
import com.noodlesandwich.quacker.ui.MessageRenderer;

public class InMemoryUser implements User {
    private final UpdatableTimeline timeline;

    public InMemoryUser(UpdatableTimeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public void publish(Message message) {
        timeline.publish(message);
    }

    @Override
    public void renderTimelineTo(MessageRenderer renderer) {
        timeline.renderTo(renderer);
    }
}

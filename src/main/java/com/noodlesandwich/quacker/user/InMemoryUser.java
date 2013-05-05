package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.UpdatableTimeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

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
    public void renderTimelineTo(TimelineRenderer renderer) {
        timeline.renderTo(renderer);
    }
}

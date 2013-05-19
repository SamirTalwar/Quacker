package com.noodlesandwich.quacker.users;

import java.util.Iterator;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.timeline.Timeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;

public class InMemoryProfile implements Profile {
    private final Timeline timeline;

    public InMemoryProfile(Timeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public void renderTimelineTo(TimelineRenderer renderer) {
        timeline.renderTo(renderer);
    }

    @Override
    public Iterator<Message> iterator() {
        return timeline.iterator();
    }
}

package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Timeline;
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
}

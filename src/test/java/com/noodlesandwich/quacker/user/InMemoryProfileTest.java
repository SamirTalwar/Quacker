package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Timeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class InMemoryProfileTest {
    private final Mockery context = new Mockery();
    private final Timeline timeline = context.mock(Timeline.class);
    private final Profile profile = new InMemoryProfile(timeline);

    @Test public void
    renders_the_messages() {
        TimelineRenderer renderer = context.mock(TimelineRenderer.class);
        context.checking(new Expectations() {{
            oneOf(timeline).renderTo(renderer);
        }});

        profile.renderTimelineTo(renderer);

        context.assertIsSatisfied();
    }
}

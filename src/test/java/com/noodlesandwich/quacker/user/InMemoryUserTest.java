package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.UpdatableTimeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class InMemoryUserTest {
    private final Mockery context = new Mockery();
    private final UpdatableTimeline timeline = context.mock(UpdatableTimeline.class);
    private final User user = new InMemoryUser(timeline);

    private final TimelineRenderer renderer = context.mock(TimelineRenderer.class);

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        Message message = new Message("Beep beep.");

        context.checking(new Expectations() {{
            oneOf(timeline).publish(message);
        }});

        user.publish(message);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        context.checking(new Expectations() {{
            oneOf(timeline).renderTo(renderer);
        }});

        user.renderTimelineTo(renderer);

        context.assertIsSatisfied();
    }
}

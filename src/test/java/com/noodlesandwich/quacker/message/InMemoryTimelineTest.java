package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class InMemoryTimelineTest {
    private final Mockery context = new Mockery();
    private final TimelineRenderer renderer = context.mock(TimelineRenderer.class);

    private final UpdatableTimeline timeline = new InMemoryTimeline();

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        Message message = new Message("Beep beep.");
        timeline.publish(message);

        context.checking(new Expectations() {{
            oneOf(renderer).render(message);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        Message one = new Message("One");
        Message two = new Message("Two");
        Message three = new Message("Three");

        timeline.publish(one);
        timeline.publish(two);
        timeline.publish(three);

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(three); inSequence(messages);
            oneOf(renderer).render(two); inSequence(messages);
            oneOf(renderer).render(one); inSequence(messages);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }
}

package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.MessageRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class InMemoryTimelineTest {
    private final Mockery context = new Mockery();
    private final MessageRenderer renderer = context.mock(MessageRenderer.class);

    private final UpdatableTimeline timeline = new InMemoryTimeline();

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        timeline.publish(new Message("Beep beep."));

        context.checking(new Expectations() {{
            oneOf(renderer).render("Beep beep.");
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        timeline.publish(new Message("One"));
        timeline.publish(new Message("Two"));
        timeline.publish(new Message("Three"));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(renderer).render("Three"); inSequence(messages);
            oneOf(renderer).render("Two"); inSequence(messages);
            oneOf(renderer).render("One"); inSequence(messages);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }
}

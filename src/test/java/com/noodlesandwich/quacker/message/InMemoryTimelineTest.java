package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class InMemoryTimelineTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2011, 12, 25, 15, 42, 23, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final TimelineRenderer renderer = context.mock(TimelineRenderer.class);

    private final UpdatableTimeline timeline = new InMemoryTimeline();

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        Message message = new Message("Beep beep.", NOW);
        timeline.publish(message);

        context.checking(new Expectations() {{
            oneOf(renderer).render(message);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        Message one = new Message("One", NOW.plusSeconds(1));
        Message two = new Message("Two", NOW.plusSeconds(2));
        Message three = new Message("Three", NOW.plusSeconds(3));

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

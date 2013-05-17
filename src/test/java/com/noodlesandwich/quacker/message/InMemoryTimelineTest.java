package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class InMemoryTimelineTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2011, 12, 25, 15, 42, 23, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final TimelineRenderer renderer = context.mock(TimelineRenderer.class);

    private final UpdatableTimeline timeline = new InMemoryTimeline();

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        final Message message = new Message(new Id(72), "Beep beep.", NOW);
        timeline.publish(message);

        context.checking(new Expectations() {{
            oneOf(renderer).render(message);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        final Message one = new Message(new Id(95), "One", NOW.plusSeconds(1));
        final Message two = new Message(new Id(97), "Two", NOW.plusSeconds(2));
        final Message three = new Message(new Id(99), "Three", NOW.plusSeconds(3));

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

    @Test public void
    caps_the_number_of_messages_at_20() {
        final List<Message> timelineMessages = new ArrayList<>();
        for (int i = 0; i < 50; ++i) {
            Message message = new Message(new Id(i), "Message " + i, NOW.plusSeconds(i));
            timelineMessages.add(message);
            timeline.publish(message);
        }
        Collections.reverse(timelineMessages);

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            for (Message message : timelineMessages.subList(0, 20)) {
                oneOf(renderer).render(message); inSequence(messages);
            }
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    is_iterable() {
        Message one = new Message(new Id(1), "One", NOW.plusSeconds(1));
        Message two = new Message(new Id(2), "Two", NOW.plusSeconds(2));
        Message three = new Message(new Id(3), "Three", NOW.plusSeconds(3));

        timeline.publish(one);
        timeline.publish(two);
        timeline.publish(three);

        assertThat(timeline, contains(three, two, one));
    }
}

package com.noodlesandwich.quacker.user;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.Timeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class InMemoryProfileTest {
    private static final Instant NOW = Instant.EPOCH;

    private final Mockery context = new Mockery();
    private final Timeline timeline = context.mock(Timeline.class);
    private final Profile profile = new InMemoryProfile(timeline);

    @Test public void
    renders_the_messages() {
        final TimelineRenderer renderer = context.mock(TimelineRenderer.class);
        context.checking(new Expectations() {{
            oneOf(timeline).renderTo(renderer);
        }});

        profile.renderTimelineTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    is_iterable() {
        Message one = new Message(1, "one", NOW);
        Message two = new Message(2, "two", NOW);
        Message three = new Message(3, "three", NOW);

        final List<Message> messages = new ArrayList<>();
        messages.add(one);
        messages.add(two);
        messages.add(three);

        context.checking(new Expectations() {{
            oneOf(timeline).iterator(); will(returnValue(messages.iterator()));
        }});

        assertThat(profile, contains(one, two, three));
    }
}

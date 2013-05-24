package com.noodlesandwich.quacker.users;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.timeline.Timeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class InMemoryProfileTest {
    private static final Instant Now = Instant.EPOCH;

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

    @SuppressWarnings("unchecked")
    @Test public void
    is_iterable() {
        User user = context.mock(User.class);
        Message one = new Message(new Id(1), user, "one", Now);
        Message two = new Message(new Id(2), user, "two", Now);
        Message three = new Message(new Id(3), user, "three", Now);

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

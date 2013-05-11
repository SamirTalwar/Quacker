package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Feed;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.UpdatableTimeline;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class InMemoryUserTest {
    private final Mockery context = new Mockery();
    private final UpdatableTimeline timeline = context.mock(UpdatableTimeline.class);
    private final Feed feed = context.mock(Feed.class);
    private final User user = new InMemoryUser(timeline, feed);

    private final TimelineRenderer timelineRenderer = context.mock(TimelineRenderer.class);

    @Test public void
    registers_that_a_user_is_following_another_user() {
        Profile profile = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(feed).follow(profile);
        }});

        user.follow(profile);

        context.assertIsSatisfied();
    }

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
    timeline_messages_are_rendered_in_reverse_chronological_order() {
        context.checking(new Expectations() {{
            oneOf(timeline).renderTo(timelineRenderer);
        }});

        user.renderTimelineTo(timelineRenderer);

        context.assertIsSatisfied();
    }
}

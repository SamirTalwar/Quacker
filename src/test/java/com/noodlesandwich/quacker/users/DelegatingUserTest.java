package com.noodlesandwich.quacker.users;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.timeline.UpdatableTimeline;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class DelegatingUserTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2012, 1, 1, 9, 0, 0, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final UpdatableTimeline timeline = context.mock(UpdatableTimeline.class);
    private final Feed feed = context.mock(Feed.class);
    private final User user = new DelegatingUser("Isha", timeline, feed);

    private final TimelineRenderer timelineRenderer = context.mock(TimelineRenderer.class);
    private final FeedRenderer feedRenderer = context.mock(FeedRenderer.class);

    @Test public void
    registers_that_a_user_is_following_another_user() {
        final Profile profile = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(feed).follow(profile);
        }});

        user.follow(profile);

        context.assertIsSatisfied();
    }

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        final Id messageId = new Id(42);
        final Message message = new Message(messageId, user, "Beep beep.", NOW);

        context.checking(new Expectations() {{
            oneOf(timeline).publish(messageId, message);
        }});

        user.publish(messageId, message);

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

    @Test public void
    keeps_track_of_followed_users_as_a_feed() {
        context.checking(new Expectations() {{
            oneOf(feed).renderTo(feedRenderer);
        }});

        user.renderFeedTo(feedRenderer);

        context.assertIsSatisfied();
    }
}

package com.noodlesandwich.quacker.users;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.google.common.collect.ImmutableList;
import com.noodlesandwich.quacker.communication.feed.Feed;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.timeline.Timeline;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.id.IdentifierSource;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class DelegatingUserTest {
    private static final Instant Now = Instant.from(ZonedDateTime.of(2012, 1, 1, 9, 0, 0, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Clock clock = Clock.fixed(Now, ZoneId.of("UTC"));
    private final IdentifierSource idSource = context.mock(IdentifierSource.class);
    private final Timeline timeline = context.mock(Timeline.class);
    private final Feed feed = context.mock(Feed.class);
    private final MessageListener messageListener = context.mock(MessageListener.class);
    private final User user = new DelegatingUser(clock, idSource, "Isha", timeline, feed, ImmutableList.of(messageListener));

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
    publishes_messages_to_a_listener() {
        final Id messageId = new Id(42);

        context.checking(new Expectations() {{
            oneOf(idSource).nextId();
            will(returnValue(messageId));
            oneOf(messageListener).publish(messageId, user, "Beep beep.", Now);
        }});

        user.publish("Beep beep.");

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

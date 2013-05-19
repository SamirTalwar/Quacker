package com.noodlesandwich.quacker.client;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.id.IdentifierSource;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.testing.Captured;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.user.Profile;
import com.noodlesandwich.quacker.user.Profiles;
import com.noodlesandwich.quacker.user.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static com.noodlesandwich.quacker.testing.CaptureParameter.captureParameter;

public class AuthenticatedClientTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 11, 14, 7, 30, 0, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Clock clock = Clock.fixed(NOW, ZoneId.of("UTC"));
    private final IdentifierSource idSource = context.mock(IdentifierSource.class);
    private final User user = context.mock(User.class);
    private final Profiles profiles = context.mock(Profiles.class);
    private final Client client = new AuthenticatedClient(clock, idSource, user, profiles);

    private final MessageRenderer messageRenderer = context.mock(MessageRenderer.class);
    private final TimelineRenderer timelineRenderer = context.mock(TimelineRenderer.class);
    private final FeedRenderer feedRenderer = context.mock(FeedRenderer.class);

    @Test public void
    publishes_messages_to_the_server() {
        final Captured<Message> message = new Captured<>();
        context.checking(new Expectations() {{
            oneOf(idSource).nextId(); will(returnValue(new Id(3)));
            oneOf(user).publish(with(any(Message.class))); will(captureParameter(0).as(message));
            oneOf(messageRenderer).render(new Id(3), user, "What's up, doc?", NOW);
        }});

        client.publish("What's up, doc?");

        message.get().renderTo(messageRenderer);

        context.assertIsSatisfied();
    }

    @Test public void
    follows_other_users() {
        final Profile uday = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(profiles).profileFor("Uday"); will(returnValue(uday));
            oneOf(user).follow(uday);
        }});

        client.follow("Uday");

        context.assertIsSatisfied();
    }

    @Test public void
    renders_a_timeline() {
        final Profile profile = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(profiles).profileFor("John"); will(returnValue(profile));
            oneOf(profile).renderTimelineTo(timelineRenderer);
        }});

        client.openTimelineOf("John", timelineRenderer);

        context.assertIsSatisfied();
    }

    @Test public void
    renders_a_feed() {
        context.checking(new Expectations() {{
            oneOf(user).renderFeedTo(feedRenderer);
        }});
        client.openFeed(feedRenderer);

        context.assertIsSatisfied();
    }
}

package com.noodlesandwich.quacker.client;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.testing.Captured;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.UserInterface;
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
    private final UserInterface userInterface = context.mock(UserInterface.class);
    private final User user = context.mock(User.class);
    private final Profiles profiles = context.mock(Profiles.class);
    private final Client client = new AuthenticatedClient(clock, userInterface, user, profiles);

    private final MessageRenderer renderer = context.mock(MessageRenderer.class);

    @Test public void
    publishes_messages_to_the_server() {
        Captured<Message> message = new Captured<>();
        context.checking(new Expectations() {{
            oneOf(user).publish(with(any(Message.class))); will(captureParameter(0).as(message));
            oneOf(renderer).render("What's up, doc?", NOW);
        }});

        client.publish("What's up, doc?");

        message.get().renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    follows_other_users() {
        Profile uday = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(profiles).profileFor("Uday"); will(returnValue(uday));
            oneOf(user).follow(uday);
        }});

        client.follow("Uday");

        context.assertIsSatisfied();
    }

    @Test public void
    renders_a_timeline() {
        Profile profile = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(profiles).profileFor("John"); will(returnValue(profile));
            oneOf(profile).renderTimelineTo(userInterface);
        }});

        client.openTimelineOf("John");

        context.assertIsSatisfied();
    }

    @Test public void
    renders_a_feed() {
        context.checking(new Expectations() {{
            oneOf(user).renderFeedTo(userInterface);
        }});
        client.openFeed();

        context.assertIsSatisfied();
    }
}

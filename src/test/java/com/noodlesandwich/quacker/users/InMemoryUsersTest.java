package com.noodlesandwich.quacker.users;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.id.IdentifierSource;
import com.noodlesandwich.quacker.testing.Captured;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static com.noodlesandwich.quacker.testing.CaptureParameter.captureParameter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class InMemoryUsersTest {
    private static final MessageListener NullMessageListener = new MessageListener() {
        @Override public void publish(Id id, Message message) { }
    };
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2009, 12, 14, 7, 30, 9, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Clock clock = Clock.fixed(NOW, ZoneId.of("UTC"));
    private final IdentifierSource idSource = context.mock(IdentifierSource.class);

    private final MessageRenderer messageRenderer = context.mock(MessageRenderer.class);

    @Test public void
    hands_over_a_previously_created_user() {
        InMemoryUsers users = new InMemoryUsers(clock, idSource, NullMessageListener);
        users.register("Govinda");
        assertThat(users.userNamed("Govinda"), is(notNullValue()));
    }

    @Test public void
    hands_over_a_read_only_profile() {
        InMemoryUsers users = new InMemoryUsers(clock, idSource, NullMessageListener);
        users.register("Madhuri");
        assertThat(users.profileFor("Madhuri"), is(notNullValue()));
    }

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_user_is_requested() {
        InMemoryUsers users = new InMemoryUsers(clock, idSource, NullMessageListener);
        users.userNamed("Hrithik");
    }

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_profile_is_requested() {
        InMemoryUsers users = new InMemoryUsers(clock, idSource, NullMessageListener);
        users.profileFor("Om");
    }

    @Test(expected=UserAlreadyExistsException.class) public void
    goes_boom_if_someone_tries_to_register_with_a_name_that_is_already_taken() {
        InMemoryUsers users = new InMemoryUsers(clock, idSource, NullMessageListener);
        users.register("Yash");
        users.register("Yash");
    }

    @Test public void
    triggers_the_given_listener_when_publishing_to_a_listener() {
        final MessageListener listener = context.mock(MessageListener.class);
        InMemoryUsers users = new InMemoryUsers(clock, idSource, listener);
        users.register("Mumtaz");

        final User mumtaz = users.userNamed("Mumtaz");
        final Id id = new Id(73);
        final Captured<Message> message = new Captured<>();

        context.checking(new Expectations() {{
            oneOf(idSource).nextId(); will(returnValue(id));
            oneOf(listener).publish(with(id), with(any(Message.class))); will(captureParameter(1).as(message));

            oneOf(messageRenderer).render(id, mumtaz, "Hey, you!", NOW);
        }});

        mumtaz.publish("Hey, you!");

        message.get().renderTo(messageRenderer);

        context.assertIsSatisfied();
    }
}

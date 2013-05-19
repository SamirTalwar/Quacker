package com.noodlesandwich.quacker.users;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.id.Id;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class InMemoryUsersTest {
    private static final MessageListener NullMessageListener = new MessageListener() {
        @Override public void publish(Id id, Message message) { }
    };
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2009, 12, 14, 7, 30, 9, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();

    @Test public void
    hands_over_a_previously_created_user() {
        InMemoryUsers users = new InMemoryUsers(NullMessageListener);
        users.register("Govinda");
        assertThat(users.userNamed("Govinda"), is(notNullValue()));
    }

    @Test public void
    hands_over_a_read_only_profile() {
        InMemoryUsers users = new InMemoryUsers(NullMessageListener);
        users.register("Madhuri");
        assertThat(users.profileFor("Madhuri"), is(notNullValue()));
    }

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_user_is_requested() {
        InMemoryUsers users = new InMemoryUsers(NullMessageListener);
        users.userNamed("Hrithik");
    }

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_profile_is_requested() {
        InMemoryUsers users = new InMemoryUsers(NullMessageListener);
        users.profileFor("Om");
    }

    @Test(expected=UserAlreadyExistsException.class) public void
    goes_boom_if_someone_tries_to_register_with_a_name_that_is_already_taken() {
        InMemoryUsers users = new InMemoryUsers(NullMessageListener);
        users.register("Yash");
        users.register("Yash");
    }

    @Test public void
    triggers_the_given_listener_when_publishing_to_a_listener() {
        final MessageListener listener = context.mock(MessageListener.class);
        InMemoryUsers users = new InMemoryUsers(listener);
        users.register("Mumtaz");

        User mumtaz = users.userNamed("Mumtaz");
        final Id id = new Id(73);
        final Message message = new Message(id, mumtaz, "Hey, you!", NOW);

        context.checking(new Expectations() {{
            oneOf(listener).publish(id, message);
        }});

        mumtaz.publish(id, message);

        context.assertIsSatisfied();
    }
}

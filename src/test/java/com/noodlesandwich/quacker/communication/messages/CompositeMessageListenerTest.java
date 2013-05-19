package com.noodlesandwich.quacker.communication.messages;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class CompositeMessageListenerTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2001, 8, 12, 5, 43, 21, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();

    @Test public void
    does_nothing_with_no_listeners() {
        MessageListener messageListener = new CompositeMessageListener();

        Id id = new Id(7);
        Message message = new Message(id, context.mock(User.class), "Bazinga.", NOW);

        messageListener.publish(id, message);

        context.assertIsSatisfied();
    }

    @Test public void
    calls_the_given_listener() {
        final MessageListener delegate = context.mock(MessageListener.class);
        MessageListener messageListener = new CompositeMessageListener(delegate);

        final Id id = new Id(91);
        final Message message = new Message(id, context.mock(User.class), "Froody.", NOW);

        context.checking(new Expectations() {{
            oneOf(delegate).publish(id, message);
        }});

        messageListener.publish(id, message);

        context.assertIsSatisfied();
    }

    @Test public void
    calls_all_listeners_in_order() {
        final MessageListener delegateA = context.mock(MessageListener.class, "MessageListener A");
        final MessageListener delegateB = context.mock(MessageListener.class, "MessageListener B");
        MessageListener messageListener = new CompositeMessageListener(delegateA, delegateB);

        final Id id = new Id(21);
        final Message message = new Message(id, context.mock(User.class), "Shiny.", NOW);

        context.checking(new Expectations() {{
            Sequence delegation = context.sequence("delegation");
            oneOf(delegateA).publish(id, message); inSequence(delegation);
            oneOf(delegateB).publish(id, message); inSequence(delegation);
        }});

        messageListener.publish(id, message);

        context.assertIsSatisfied();
    }
}

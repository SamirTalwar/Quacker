package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

public class InMemoryUserTest {
    private final Mockery context = new Mockery();
    private MessageRenderer renderer = context.mock(MessageRenderer.class);

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        User user = new InMemoryUser();
        user.publish(new Message("Beep beep."));

        context.checking(new Expectations() {{
            oneOf(renderer).render("Beep beep.");
        }});

        user.renderTimelineTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        User user = new InMemoryUser();
        user.publish(new Message("One"));
        user.publish(new Message("Two"));
        user.publish(new Message("Three"));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(renderer).render("Three"); inSequence(messages);
            oneOf(renderer).render("Two"); inSequence(messages);
            oneOf(renderer).render("One"); inSequence(messages);
        }});

        user.renderTimelineTo(renderer);

        context.assertIsSatisfied();
    }
}

package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.message.Timeline;
import com.noodlesandwich.quacker.message.Timelines;
import com.noodlesandwich.quacker.testing.Captured;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.UserInterface;
import com.noodlesandwich.quacker.user.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static com.noodlesandwich.quacker.testing.CaptureParameter.captureParameter;

public class AuthenticatedClientTest {
    private final Mockery context = new Mockery();
    private final UserInterface userInterface = context.mock(UserInterface.class);
    private final User user = context.mock(User.class);
    private final Timelines timelines = context.mock(Timelines.class);
    private final Client client = new AuthenticatedClient(userInterface, user, timelines);

    private final MessageRenderer renderer = context.mock(MessageRenderer.class);

    @Test public void
    publishes_messages_to_the_server() {
        Captured<Message> message = new Captured<>();
        context.checking(new Expectations() {{
            oneOf(user).publish(with(any(Message.class))); will(captureParameter(0).as(message));
            oneOf(renderer).render("What's up, doc?");
        }});

        client.publish("What's up, doc?");

        message.get().renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    renders_a_timeline() {
        Timeline timeline = context.mock(Timeline.class);
        context.checking(new Expectations() {{
            oneOf(timelines).forUser("John"); will(returnValue(timeline));
            oneOf(timeline).renderTo(userInterface);
        }});

        client.openTimelineOf("John");

        context.assertIsSatisfied();
    }
}
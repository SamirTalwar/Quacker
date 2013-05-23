package com.noodlesandwich.quacker.communication.conversations;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.NonExistentMessageException;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.ConversationRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.users.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static java.time.temporal.ChronoUnit.HOURS;

public class ConversationGraphTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 5, 19, 18, 19, 25, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final ConversationGraph conversations = new ConversationGraph();

    private final MessageRenderer messageRenderer = context.mock(MessageRenderer.class);
    private final ConversationRenderer renderer = new ConversationRenderer() {
        @Override public void render(Message message) {
            message.renderTo(messageRenderer);
        }
    };

    @Test public void
    a_message_with_no_connected_replies_is_its_own_conversation() {
        final Id messageId = new Id(3);
        final User pooja = context.mock(User.class);

        context.checking(new Expectations() {{
            allowing(pooja).getUsername(); will(returnValue("Pooja"));
        }});

        conversations.publish(messageId, pooja, "Boom goes the dynamite.", NOW);

        context.checking(new Expectations() {{
            oneOf(messageRenderer).render(messageId, pooja, "Boom goes the dynamite.", NOW);
        }});

        Conversation conversation = conversations.conversationAround(messageId);
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    a_message_that_replies_to_another_message_becomes_part_of_its_conversation() {
        final User rani = context.mock(User.class, "User A");
        final User sunil = context.mock(User.class, "User B");

        context.checking(new Expectations() {{
            allowing(rani).getUsername(); will(returnValue("Rani"));
            allowing(sunil).getUsername(); will(returnValue("Sunil"));
        }});

        final Id messageAId = new Id(9);
        final Id messageBId = new Id(43);

        conversations.publish(messageAId, rani, "Who's there?", NOW.plus(1, HOURS));
        conversations.publish(messageBId, sunil, "@Rani I'm here.", NOW.plus(2, HOURS));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(messageRenderer).render(messageAId, rani, "Who's there?", NOW.plus(1, HOURS)); inSequence(messages);
            oneOf(messageRenderer).render(messageBId, sunil, "@Rani I'm here.", NOW.plus(2, HOURS)); inSequence(messages);
        }});

        Conversation conversation = conversations.conversationAround(messageAId);
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test(expected=NonExistentMessageException.class) public void
    a_non_existent_message_results_in_an_exception() {
        conversations.conversationAround(new Id(88));
    }
}

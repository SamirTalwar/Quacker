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
import org.junit.Test;

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
        final User author = context.mock(User.class);

        conversations.publish(messageId, author, "Boom goes the dynamite.", NOW);

        context.checking(new Expectations() {{
            oneOf(messageRenderer).render(messageId, author, "Boom goes the dynamite.", NOW);
        }});

        Conversation conversation = conversations.conversationAround(messageId);
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test(expected=NonExistentMessageException.class) public void
    a_non_existent_message_results_in_an_exception() {
        conversations.conversationAround(new Id(88));
    }
}

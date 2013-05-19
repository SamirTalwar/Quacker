package com.noodlesandwich.quacker.communication.conversations;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.ConversationRenderer;
import com.noodlesandwich.quacker.users.User;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ConversationGraphTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 5, 19, 18, 19, 25, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final ConversationGraph conversations = new ConversationGraph();

    private final List<Message> conversationMessages = new ArrayList<>();
    private final ConversationRenderer renderer = new ConversationRenderer() {
        @Override public void render(Message message) {
            conversationMessages.add(message);
        }
    };

    @Test public void
    a_message_with_no_connected_replies_is_its_own_conversation() {
        Id messageId = new Id(3);
        User author = context.mock(User.class);
        Message message = new Message(messageId, author, "Boom goes the dynamite.", NOW);

        conversations.register(messageId, message);

        Conversation conversation = conversations.conversationAround(messageId);
        conversation.renderConversationTo(renderer);

        assertThat(conversationMessages, contains(message));
    }
}

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

public class SortedConversationTest {
    private static final Instant Now = Instant.from(ZonedDateTime.of(2011, 1, 1, 4, 56, 3, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final User me = context.mock(User.class);

    private final List<Message> conversationMessages = new ArrayList<>();
    private ConversationRenderer renderer = new ConversationRenderer() {
        @Override public void render(Message message) {
            conversationMessages.add(message);
        }
    };

    @Test public void
    renders_the_messages() {
        Message one = new Message(new Id(1), me, "One", Now.plusSeconds(1));
        Message two = new Message(new Id(2), me, "Two", Now.plusSeconds(2));
        Message three = new Message(new Id(3), me, "Three", Now.plusSeconds(3));

        Conversation conversation = new SortedConversation(one, two, three);
        conversation.renderConversationTo(renderer);

        assertThat(conversationMessages, contains(one, two, three));
    }

    @Test public void
    sorts_the_messages() {
        Message one = new Message(new Id(1), me, "One", Now.plusSeconds(1));
        Message two = new Message(new Id(2), me, "Two", Now.plusSeconds(2));
        Message three = new Message(new Id(3), me, "Three", Now.plusSeconds(3));

        Conversation conversation = new SortedConversation(three, one, two);
        conversation.renderConversationTo(renderer);

        assertThat(conversationMessages, contains(one, two, three));
    }
}

package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.communication.conversations.Conversations;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.server.Server;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConversationDownloaderTest {
    private final Mockery context = new Mockery();
    private final Server server = context.mock(Server.class);
    private final Conversations conversations = new ConversationDownloader(server);

    @Test public void
    downloads_conversations_from_the_server() {
        final Id messageId = new Id(133);
        final Conversation conversation = context.mock(Conversation.class);

        context.checking(new Expectations() {{
            oneOf(server).conversationAround(messageId); will(returnValue(conversation));
        }});

        assertThat(conversations.conversationAround(messageId), is(conversation));

        context.assertIsSatisfied();
    }
}

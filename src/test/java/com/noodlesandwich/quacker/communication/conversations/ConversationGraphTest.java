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
import static java.time.temporal.ChronoUnit.MINUTES;

public class ConversationGraphTest {
    private static final Instant Now = Instant.from(ZonedDateTime.of(2013, 5, 19, 18, 19, 25, 0, ZoneId.of("UTC")));

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

        conversations.publish(messageId, pooja, "Boom goes the dynamite.", Now);

        context.checking(new Expectations() {{
            oneOf(messageRenderer).render(messageId, pooja, "Boom goes the dynamite.", Now);
        }});

        Conversation conversation = conversations.conversationAround(messageId);
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    a_message_that_replies_to_another_message_becomes_part_of_its_conversation() {
        final User rani = context.mock(User.class, "Rani");
        final User sunil = context.mock(User.class, "Sunil");

        context.checking(new Expectations() {{
            allowing(rani).getUsername(); will(returnValue("Rani"));
            allowing(sunil).getUsername(); will(returnValue("Sunil"));
        }});

        final Id messageAId = new Id(9);
        final Id messageBId = new Id(43);

        conversations.publish(messageAId, rani, "Who's there?", Now.plus(1, HOURS));
        conversations.publish(messageBId, sunil, "@Rani I'm here.", Now.plus(2, HOURS));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(messageRenderer).render(messageAId, rani, "Who's there?", Now.plus(1, HOURS)); inSequence(messages);
            oneOf(messageRenderer).render(messageBId, sunil, "@Rani I'm here.", Now.plus(2, HOURS)); inSequence(messages);
        }});

        Conversation conversation = conversations.conversationAround(messageAId);
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    multiple_replies_can_be_made_to_a_single_message() {
        final User tarun = context.mock(User.class, "Tarun");
        final User uttan = context.mock(User.class, "Uttan");
        final User vikram = context.mock(User.class, "Vikram");
        final User yogeeta = context.mock(User.class, "Yogeeta");

        context.checking(new Expectations() {{
            allowing(tarun).getUsername(); will(returnValue("Tarun"));
            allowing(uttan).getUsername(); will(returnValue("Uttan"));
            allowing(vikram).getUsername(); will(returnValue("Vikram"));
            allowing(yogeeta).getUsername(); will(returnValue("Yogeeta"));
        }});

        conversations.publish(new Id(1), tarun, "Who's hungry?", Now.plus(15, MINUTES));
        conversations.publish(new Id(2), uttan, "@Tarun Me. Sushi?", Now.plus(20, MINUTES));
        conversations.publish(new Id(3), vikram, "@Uttan @Tarun What about Chinese?", Now.plus(22, MINUTES));
        conversations.publish(new Id(4), tarun, "@Vikram @Uttan I hate Chinese. Too oily.", Now.plus(23, MINUTES));
        conversations.publish(new Id(5), yogeeta, "SO HUNGRY.", Now.plus(24, MINUTES));
        conversations.publish(new Id(6), uttan, "@Vikram @Tarun Sure, I'm up for Chinese.", Now.plus(24, MINUTES));
        conversations.publish(new Id(7), uttan, "@Tarun @Vikram Oh, fair enough. So… sushi?", Now.plus(28, MINUTES));
        conversations.publish(new Id(8), vikram, "@Tarun OK, let's do sushi like @Uttan says.", Now.plus(29, MINUTES));
        conversations.publish(new Id(9), yogeeta, "@Tarun Where's my invitation. Sushi sounds great!", Now.plus(30, MINUTES));
        conversations.publish(new Id(10), tarun, "@Yogeeta @Vikram @Uttan Great. See you in five.", Now.plus(32, MINUTES));
        conversations.publish(new Id(11), tarun, "Woo! Sushi o'clock!", Now.plus(33, MINUTES));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(messageRenderer).render(new Id(1), tarun, "Who's hungry?", Now.plus(15, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(2), uttan, "@Tarun Me. Sushi?", Now.plus(20, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(3), vikram, "@Uttan @Tarun What about Chinese?", Now.plus(22, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(4), tarun, "@Vikram @Uttan I hate Chinese. Too oily.", Now.plus(23, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(6), uttan, "@Vikram @Tarun Sure, I'm up for Chinese.", Now.plus(24, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(7), uttan, "@Tarun @Vikram Oh, fair enough. So… sushi?", Now.plus(28, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(8), vikram, "@Tarun OK, let's do sushi like @Uttan says.", Now.plus(29, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(9), yogeeta, "@Tarun Where's my invitation. Sushi sounds great!", Now.plus(30, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(10), tarun, "@Yogeeta @Vikram @Uttan Great. See you in five.", Now.plus(32, MINUTES)); inSequence(messages);
        }});

        Conversation conversation = conversations.conversationAround(new Id(1));
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    conversations_can_be_loaded_from_any_message_in_the_conversation() {
        final User mallika = context.mock(User.class, "Mallika");
        final User nazir = context.mock(User.class, "Nazir");

        context.checking(new Expectations() {{
            allowing(mallika).getUsername(); will(returnValue("Mallika"));
            allowing(nazir).getUsername(); will(returnValue("Nazir"));
        }});

        conversations.publish(new Id(101), mallika, "Hey, @Nazir, wanna dance?", Now.plus(5, MINUTES));
        conversations.publish(new Id(102), nazir, "@Mallika You know I don't dance.", Now.plus(6, MINUTES));
        conversations.publish(new Id(103), mallika, "@Nazir I've never seen you try.", Now.plus(8, MINUTES));
        conversations.publish(new Id(104), mallika, "@Nazir I bet you have some incredible twirls.", Now.plus(9, MINUTES));
        conversations.publish(new Id(105), nazir, "@Mallika *blushes*", Now.plus(10, MINUTES));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(messageRenderer).render(new Id(101), mallika, "Hey, @Nazir, wanna dance?", Now.plus(5, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(102), nazir, "@Mallika You know I don't dance.", Now.plus(6, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(104), mallika, "@Nazir I bet you have some incredible twirls.", Now.plus(9, MINUTES)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(105), nazir, "@Mallika *blushes*", Now.plus(10, MINUTES)); inSequence(messages);
        }});

        Conversation conversation = conversations.conversationAround(new Id(104));
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    only_go_ten_messages_in_each_direction() {
        final User farooq = context.mock(User.class, "Farooq");
        final User geeta = context.mock(User.class, "Geeta");

        context.checking(new Expectations() {{
            allowing(farooq).getUsername(); will(returnValue("Farooq"));
            allowing(geeta).getUsername(); will(returnValue("Geeta"));
        }});

        for (int i = 0; i < 50; ++i) {
            conversations.publish(new Id(i * 3 + 1), farooq, "@Geeta", Now.plusSeconds(i * 3 + 1));
            conversations.publish(new Id(i * 3 + 2), geeta, "@Farooq A", Now.plusSeconds(i * 3 + 2));
            conversations.publish(new Id(i * 3 + 3), geeta, "@Farooq B", Now.plusSeconds(i * 3 + 3));
        }

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(messageRenderer).render(new Id(85), farooq, "@Geeta", Now.plusSeconds(85)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(87), geeta, "@Farooq B", Now.plusSeconds(87)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(88), farooq, "@Geeta", Now.plusSeconds(88)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(90), geeta, "@Farooq B", Now.plusSeconds(90)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(91), farooq, "@Geeta", Now.plusSeconds(91)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(93), geeta, "@Farooq B", Now.plusSeconds(93)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(94), farooq, "@Geeta", Now.plusSeconds(94)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(96), geeta, "@Farooq B", Now.plusSeconds(96)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(97), farooq, "@Geeta", Now.plusSeconds(97)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(99), geeta, "@Farooq B", Now.plusSeconds(99)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(100), farooq, "@Geeta", Now.plusSeconds(100)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(101), geeta, "@Farooq A", Now.plusSeconds(101)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(102), geeta, "@Farooq B", Now.plusSeconds(102)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(103), farooq, "@Geeta", Now.plusSeconds(103)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(104), geeta, "@Farooq A", Now.plusSeconds(104)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(105), geeta, "@Farooq B", Now.plusSeconds(105)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(106), farooq, "@Geeta", Now.plusSeconds(106)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(107), geeta, "@Farooq A", Now.plusSeconds(107)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(108), geeta, "@Farooq B", Now.plusSeconds(108)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(109), farooq, "@Geeta", Now.plusSeconds(109)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(110), geeta, "@Farooq A", Now.plusSeconds(110)); inSequence(messages);
        }});

        Conversation conversation = conversations.conversationAround(new Id(100));
        conversation.renderConversationTo(renderer);

        context.assertIsSatisfied();
    }

    @Test(expected=NonExistentMessageException.class) public void
    a_non_existent_message_results_in_an_exception() {
        conversations.conversationAround(new Id(88));
    }
}

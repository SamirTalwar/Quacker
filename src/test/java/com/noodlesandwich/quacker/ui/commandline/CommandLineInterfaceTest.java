package com.noodlesandwich.quacker.ui.commandline;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ListIterator;
import com.google.common.collect.ImmutableList;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.client.Login;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.users.User;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.Test;

import static java.time.temporal.ChronoUnit.MINUTES;

public class CommandLineInterfaceTest {
    private static final Instant Now = Instant.from(ZonedDateTime.of(2005, 6, 17, 9, 30, 45, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Sequence io = context.sequence("I/O");
    private final CommandLine commandLine = context.mock(CommandLine.class);
    private final Login login = context.mock(Login.class);
    private final CommandLineInterface cli = new CommandLineInterface(commandLine, login);

    @Test public void
    prompts_the_user_to_log_in() throws IOException {
        final Client client = context.mock(Client.class);
        context.checking(new Expectations() {{
            oneOf(login).loginAs("Lokesh"); will(returnValue(client));

            oneOf(commandLine).write("Login: "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("Lokesh")); inSequence(io);
            oneOf(commandLine).writeLine("Logged in successfully."); inSequence(io);
        }});

        thenQuit();

        cli.run();

        context.assertIsSatisfied();
    }

    @Test public void
    publishes_a_message() throws IOException {
        final Client client = context.mock(Client.class);
        loginAs("Mahesh", client);

        context.checking(new Expectations() {{
            oneOf(commandLine).write("> "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("p Hey, what's up?")); inSequence(io);

            oneOf(client).publish("Hey, what's up?");
        }});

        thenQuit();

        cli.run();

        context.assertIsSatisfied();
    }

    @Test public void
    shows_the_recent_posts_from_a_user() throws IOException {
        final Client client = context.mock(Client.class);
        loginAs("Neha", client);

        final User omi = context.mock(User.class);
        final List<Message> messages = ImmutableList.of(
                new Message(new Id(3), omi, "Hey, is anyone here?", Now.plus(5, MINUTES)),
                new Message(new Id(4), omi, "I guess not.", Now.plus(15, MINUTES)),
                new Message(new Id(7), omi, "That's a shame. It would be nice if someone said hi.", Now.plus(17, MINUTES)),
                new Message(new Id(19), omi, "I guess I'm on my own.", Now.plus(35, MINUTES)),
                new Message(new Id(200), omi, "Hey, is anyone here now?", Now.plus(2, ChronoUnit.DAYS))
        );

        context.checking(new Expectations() {{
            allowing(omi).getUsername(); will(returnValue("Omi"));

            oneOf(client).openTimelineOf(with("Omi"), with(any(TimelineRenderer.class))); will(renderMessages(messages));

            oneOf(commandLine).write("> "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("t Omi")); inSequence(io);
            oneOf(commandLine).writeLine("200 Omi: Hey, is anyone here now?"); inSequence(io);
            oneOf(commandLine).writeLine("19 Omi: I guess I'm on my own."); inSequence(io);
            oneOf(commandLine).writeLine("7 Omi: That's a shame. It would be nice if someone said hi."); inSequence(io);
            oneOf(commandLine).writeLine("4 Omi: I guess not."); inSequence(io);
            oneOf(commandLine).writeLine("3 Omi: Hey, is anyone here?"); inSequence(io);
        }});

        thenQuit();

        cli.run();

        context.assertIsSatisfied();
    }

    @Test public void
    prompts_a_lot() throws IOException {
        final Client client = context.mock(Client.class);
        loginAs("Preity", client);

        final User preity = context.mock(User.class);
        context.checking(new Expectations() {{
            allowing(preity).getUsername(); will(returnValue("Preity"));

            oneOf(client).publish("Looking good, yaar.");
            oneOf(client).publish("Totally.");

            oneOf(client).openTimelineOf(with("Preity"), with(any(TimelineRenderer.class))); will(renderMessages(ImmutableList.of(
                    new Message(new Id(15), preity, "I made a Quacker account!", Now.plus(13, MINUTES)),
                    new Message(new Id(17), preity, "Looking good, yaar.", Now.plus(19, MINUTES)),
                    new Message(new Id(18), preity, "Totally.", Now.plus(21, MINUTES))
            )));

            oneOf(commandLine).write("> "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("p Looking good, yaar.")); inSequence(io);

            oneOf(commandLine).write("> "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("p Totally.")); inSequence(io);

            oneOf(commandLine).write("> "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("t Preity")); inSequence(io);
            oneOf(commandLine).writeLine("18 Preity: Totally."); inSequence(io);
            oneOf(commandLine).writeLine("17 Preity: Looking good, yaar."); inSequence(io);
            oneOf(commandLine).writeLine("15 Preity: I made a Quacker account!"); inSequence(io);
        }});

        thenQuit();

        cli.run();

        context.assertIsSatisfied();
    }

    private void loginAs(final String username, final Client client) throws IOException {
        context.checking(new Expectations() {{
            oneOf(login).loginAs(username); will(returnValue(client));

            oneOf(commandLine).write("Login: "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue(username)); inSequence(io);
            oneOf(commandLine).writeLine("Logged in successfully."); inSequence(io);
        }});
    }

    private void thenQuit() throws IOException {
        context.checking(new Expectations() {{
            oneOf(commandLine).write("> "); inSequence(io);
            oneOf(commandLine).read(); will(returnValue("q")); inSequence(io);
        }});
    }

    private static Action renderMessages(final List<Message> messages) {
        return new Action() {
            @Override
            public void describeTo(Description description) {
                description.appendText("render the messages ")
                           .appendValueList("", ", ", "", messages);
            }

            @Override
            public Object invoke(Invocation invocation) {
                TimelineRenderer renderer = (TimelineRenderer) invocation.getParameter(1);
                ListIterator<Message> descendingIterator = messages.listIterator(messages.size());
                while (descendingIterator.hasPrevious()) {
                    renderer.render(descendingIterator.previous());
                }
                return null;
            }
        };
    }
}

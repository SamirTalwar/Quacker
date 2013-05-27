package com.noodlesandwich.quacker.ui.commandline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.Test;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class CommandLineInterfaceTest {
    private static final String LineSeparator = System.getProperty("line.separator");
    private static final Instant Now = Instant.from(ZonedDateTime.of(2005, 6, 17, 9, 30, 45, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Login login = context.mock(Login.class);
    private final PipedOutputStream inputWriter;
    private final ConsumableByteArrayOutputStream outputReader;
    private final CommandLineInterface cli;

    public CommandLineInterfaceTest() throws IOException {
        PipedInputStream input = new PipedInputStream();
        inputWriter = new PipedOutputStream(input);
        outputReader = new ConsumableByteArrayOutputStream();
        cli = new CommandLineInterface(login, new InputStreamReader(input), new OutputStreamWriter(outputReader));
    }

    @Test public void
    prompts_the_user_to_log_in() throws IOException {
        final Client client = context.mock(Client.class);
        context.checking(new Expectations() {{
            oneOf(login).loginAs("Lokesh"); will(returnValue(client));
        }});

        cli.next();
        read("Login: ");
        writeLine("Lokesh");

        cli.next();
        readLine("Logged in successfully.");

        context.assertIsSatisfied();
    }

    @Test public void
    publishes_a_message() throws IOException {
        final Client client = context.mock(Client.class);
        loginAs("Mahesh", client);

        read("> ");
        writeLine("p Hey, what's up?");

        context.checking(new Expectations() {{
            oneOf(client).publish("Hey, what's up?");
        }});

        cli.next();

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

        read("> ");
        writeLine("t Omi");

        context.checking(new Expectations() {{
            allowing(omi).getUsername(); will(returnValue("Omi"));
            oneOf(client).openTimelineOf(with("Omi"), with(any(TimelineRenderer.class))); will(renderMessages(messages));
        }});

        cli.next();
        readLine("200 Omi: Hey, is anyone here now?");
        readLine("19 Omi: I guess I'm on my own.");
        readLine("7 Omi: That's a shame. It would be nice if someone said hi.");
        readLine("4 Omi: I guess not.");
        readLine("3 Omi: Hey, is anyone here?");

        context.assertIsSatisfied();
    }

    @Test public void
    quits_on_demand() throws IOException {
        final Client client = context.mock(Client.class);
        loginAs("Zaheera", client);

        read("> ");
        writeLine("q");

        assertThat(cli.next(), is(false));
    }

    private void loginAs(final String username, final Client client) throws IOException {
        context.checking(new Expectations() {{
            oneOf(login).loginAs(username); will(returnValue(client));
        }});

        cli.next();
        read("Login: ");
        writeLine(username);

        cli.next();
        readLine("Logged in successfully.");
    }

    private void read(String string) {
        assertThat(outputReader.toString(), startsWith(string));
        outputReader.consume(string.length());
    }

    private void readLine(String string) {
        read(string + "\n");
    }

    private void writeLine(String string) {
        try {
            inputWriter.write(string.getBytes());
            inputWriter.write(LineSeparator.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ConsumableByteArrayOutputStream extends ByteArrayOutputStream {
        public void consume(int bytes) {
            count -= bytes;
            byte[] newBuf = new byte[buf.length];
            System.arraycopy(buf, bytes, newBuf, 0, count);
            buf = newBuf;
        }
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

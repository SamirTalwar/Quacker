package com.noodlesandwich.quacker.ui.commandline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.client.Login;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class CommandLineInterfaceTest {
    private static final String LineSeparator = System.getProperty("line.separator");

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
    }

    @Test public void
    quits_on_demand() throws IOException {
        final Client client = context.mock(Client.class);
        loginAs("Neha", client);

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
}

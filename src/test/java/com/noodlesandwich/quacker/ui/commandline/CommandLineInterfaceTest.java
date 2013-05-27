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

public class CommandLineInterfaceTest {
    private static final String LineSeparator = System.getProperty("line.separator");

    private final Mockery context = new Mockery();
    private final Login login = context.mock(Login.class);
    private final PipedOutputStream inputWriter;
    private final ByteArrayOutputStream outputReader;
    private final CommandLineInterface cli;

    public CommandLineInterfaceTest() throws IOException {
        PipedInputStream input = new PipedInputStream();
        inputWriter = new PipedOutputStream(input);
        outputReader = new ByteArrayOutputStream();
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
        read("Logged in successfully.");
    }

    private void read(String string) {
        assertThat(outputReader.toString(), is(string));
        outputReader.reset();
    }

    private void writeLine(String string) {
        try {
            inputWriter.write(string.getBytes());
            inputWriter.write(LineSeparator.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.noodlesandwich.quacker.ui;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.quacker.message.Message;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class CommandLineUserInterfaceTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 1, 10, 8, 12, 16, 0, ZoneId.of("UTC")));

    private final OutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = new PrintStream(outputStream);
    private final UserInterface userInterface = new CommandLineUserInterface(printStream);

    @Test public void
    renders_a_timeline_as_messages() {
        Message message = new Message("Bonjour, amigo.", NOW);

        userInterface.render(message);

        assertThat(output(), contains("Bonjour, amigo."));
    }

    private List<String> output() {
        return Arrays.asList(outputStream.toString().split("\n"));
    }
}

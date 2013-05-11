package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class MessageTest {
    private static final ZonedDateTime NOW = ZonedDateTime.of(2013, 5, 11, 17, 25, 8, 0, ZoneId.of("Europe/London"));

    private final Mockery context = new Mockery();
    private final MessageRenderer renderer = context.mock(MessageRenderer.class);

    @Test public void
    renders() {
        Instant now = Instant.from(NOW);
        context.checking(new Expectations() {{
            oneOf(renderer).render("Boop.", now);
        }});

        Message message = new Message("Boop.", now);
        message.renderTo(renderer);

        context.assertIsSatisfied();
    }
}

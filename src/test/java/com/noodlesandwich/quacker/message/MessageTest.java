package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

public class MessageTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 5, 11, 17, 25, 8, 0, ZoneId.of("Europe/London")));

    private final Mockery context = new Mockery();
    private final MessageRenderer renderer = context.mock(MessageRenderer.class);

    @Test public void
    renders() {
        context.checking(new Expectations() {{
            oneOf(renderer).render("Boop.", NOW);
        }});

        Message message = new Message("Boop.", NOW);
        message.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    two_messages_with_the_same_text_and_timestamp_are_equal() {
        Message messageA = new Message("Words are here.", NOW);
        Message messageB = new Message("Words are here.", NOW);

        assertThat(messageA, is(equalTo(messageB)));
        assertThat(messageA.hashCode(), is(messageB.hashCode()));
    }

    @Test public void
    two_messages_with_different_text_are_not_equal() {
        Message messageA = new Message("Words are here.", NOW);
        Message messageB = new Message("Words are not here.", NOW);

        assertThat(messageA, is(not(equalTo(messageB))));
        assertThat(messageA.hashCode(), is(not(messageB.hashCode())));
    }

    @Test public void
    two_messages_with_different_timestamps_are_not_equal() {
        Message messageA = new Message("Blah blah blah.", NOW.plusSeconds(15));
        Message messageB = new Message("Blah blah blah.", NOW.plusSeconds(30));

        assertThat(messageA, is(not(equalTo(messageB))));
        assertThat(messageA.hashCode(), is(not(messageB.hashCode())));
    }

    @Test public void
    two_messages_compare_by_timestamp() {
        Message lesserMessage = new Message("Text.", NOW.plusSeconds(15));
        Message greaterMessage = new Message("Text.", NOW.plusSeconds(30));

        assertThat(lesserMessage, is(lessThan(greaterMessage)));
        assertThat(greaterMessage, is(greaterThan(lesserMessage)));
    }

    @Test public void
    two_messages_compare_by_text_if_the_timestamps_are_identical() {
        Message lesserMessage = new Message("Words ending with aardvark.", NOW.plusSeconds(60));
        Message greaterMessage = new Message("Words ending with zebra.", NOW.plusSeconds(60));

        assertThat(lesserMessage, is(lessThan(greaterMessage)));
        assertThat(greaterMessage, is(greaterThan(lesserMessage)));
    }

    @Test public void
    equal_messages_compare_as_the_same() {
        Message lesserMessage = new Message("Look!", NOW.plusSeconds(60));
        Message greaterMessage = new Message("Look!", NOW.plusSeconds(60));

        assertThat(lesserMessage, comparesEqualTo(greaterMessage));
        assertThat(greaterMessage, comparesEqualTo(lesserMessage));
    }
}
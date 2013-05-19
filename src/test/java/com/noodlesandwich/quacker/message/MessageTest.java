package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.user.User;
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
        final User user = context.mock(User.class);
        context.checking(new Expectations() {{
            oneOf(renderer).render(new Id(7), user, "Boop.", NOW);
        }});

        Message message = new Message(new Id(7), user, "Boop.", NOW);
        message.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test(expected=EmptyMessageException.class) public void
    cannot_be_empty() {
        User user = context.mock(User.class);
        new Message(new Id(12), user, "", NOW);
    }

    @Test(expected=MessageTooLongException.class) public void
    cannot_be_more_than_140_characters() {
        User user = context.mock(User.class);
        new Message(new Id(99), user, stringOfLength(141), NOW);
    }

    @Test public void
    two_messages_with_the_same_ID_are_equal() {
        User user = context.mock(User.class);
        Message messageA = new Message(new Id(55), user, "Words are here.", NOW);
        Message messageB = new Message(new Id(55), user, "Words are here.", NOW);

        assertThat(messageA, is(equalTo(messageB)));
        assertThat(messageA.hashCode(), is(messageB.hashCode()));
    }

    @Test public void
    two_messages_with_different_IDs_are_not_equal() {
        User user = context.mock(User.class);
        Message messageA = new Message(new Id(92), user, "Look!", NOW);
        Message messageB = new Message(new Id(29), user, "Look!", NOW);

        assertThat(messageA, is(not(equalTo(messageB))));
        assertThat(messageA.hashCode(), is(not(messageB.hashCode())));
    }

    @Test public void
    two_messages_compare_by_timestamp() {
        User user = context.mock(User.class);
        Message lesserMessage = new Message(new Id(94), user, "Text.", NOW.plusSeconds(15));
        Message greaterMessage = new Message(new Id(94), user, "Text.", NOW.plusSeconds(30));

        assertThat(lesserMessage, is(lessThan(greaterMessage)));
        assertThat(greaterMessage, is(greaterThan(lesserMessage)));
    }

    @Test public void
    two_messages_compare_by_text_if_the_timestamps_are_identical() {
        User user = context.mock(User.class);
        Message lesserMessage = new Message(new Id(49), user, "Words ending with aardvark.", NOW.plusSeconds(60));
        Message greaterMessage = new Message(new Id(49), user, "Words ending with zebra.", NOW.plusSeconds(60));

        assertThat(lesserMessage, is(lessThan(greaterMessage)));
        assertThat(greaterMessage, is(greaterThan(lesserMessage)));
    }

    @Test public void
    equal_messages_compare_as_the_same() {
        User user = context.mock(User.class);
        Message lesserMessage = new Message(new Id(72), user, "Look!", NOW.plusSeconds(60));
        Message greaterMessage = new Message(new Id(72), user, "Look!", NOW.plusSeconds(60));

        assertThat(lesserMessage, comparesEqualTo(greaterMessage));
        assertThat(greaterMessage, comparesEqualTo(lesserMessage));
    }

    private static String stringOfLength(int length) {
        Random random = new Random();
        char[] characters = new char[length];
        for (int i = 0; i < length; ++i) {
            char character;
            do {
                character = (char) random.nextInt();
            } while (!Character.isLetterOrDigit(character));
            characters[i] = character;
        }
        return String.valueOf(characters);
    }
}

package com.noodlesandwich.quacker.communication;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.timeline.InMemoryTimeline;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.testing.Captured;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.users.User;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class InMemoryTimelineTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2011, 12, 25, 15, 42, 23, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final MessageRenderer messageRenderer = context.mock(MessageRenderer.class);
    private final TimelineRenderer renderer = new TimelineRenderer() {
        @Override public void render(Message message) {
            message.renderTo(messageRenderer);
        }
    };

    private final InMemoryTimeline timeline = new InMemoryTimeline();

    @Test public void
    publishes_messages_to_an_in_memory_timeline() {
        final User user = context.mock(User.class);
        final Id id = new Id(72);
        timeline.publish(id, user, "Beep beep.", NOW);

        context.checking(new Expectations() {{
            oneOf(messageRenderer).render(id, user, "Beep beep.", NOW);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    messages_are_rendered_in_reverse_chronological_order() {
        final User user = context.mock(User.class);
        timeline.publish(new Id(95), user, "One", NOW.plusSeconds(1));
        timeline.publish(new Id(97), user, "Two", NOW.plusSeconds(2));
        timeline.publish(new Id(99), user, "Three", NOW.plusSeconds(3));

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            oneOf(messageRenderer).render(new Id(99), user, "Three", NOW.plusSeconds(3)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(97), user, "Two", NOW.plusSeconds(2)); inSequence(messages);
            oneOf(messageRenderer).render(new Id(95), user, "One", NOW.plusSeconds(1)); inSequence(messages);
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    caps_the_number_of_messages_at_20() {
        final User user = context.mock(User.class);
        for (int i = 0; i < 50; ++i) {
            timeline.publish(new Id(i), user, "Message " + i, NOW.plusSeconds(i));
        }

        context.checking(new Expectations() {{
            Sequence messages = context.sequence("messages");
            for (int i = 49; i >= 30; --i) {
                oneOf(messageRenderer).render(new Id(i), user, "Message " + i, NOW.plusSeconds(i)); inSequence(messages);
            }
        }});

        timeline.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test public void
    is_iterable() {
        User user = context.mock(User.class);
        Matcher<Message> one = aMessage(new Id(1), user, "One", NOW.plusSeconds(1));
        Matcher<Message> two = aMessage(new Id(2), user, "Two", NOW.plusSeconds(2));
        Matcher<Message> three = aMessage(new Id(3), user, "Three", NOW.plusSeconds(3));

        timeline.publish(new Id(1), user, "One", NOW.plusSeconds(1));
        timeline.publish(new Id(2), user, "Two", NOW.plusSeconds(2));
        timeline.publish(new Id(3), user, "Three", NOW.plusSeconds(3));

        assertThat(timeline, contains(three, two, one));
    }

    @Test(expected=UnsupportedOperationException.class) public void
    is_not_modifiable_through_the_iterator() {
        User user = context.mock(User.class);
        timeline.publish(new Id(55), user, "Fifty-five is a big number", NOW);

        Iterator<Message> iterator = timeline.iterator();
        iterator.next();
        iterator.remove();
    }

    private static Matcher<Message> aMessage(final Id expectedId, final User expectedAuthor, final String expectedText, final Instant expectedTimestamp) {
        return new TypeSafeDiagnosingMatcher<Message>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("a message")
                           .appendText(" with the ID ").appendValue(expectedId)
                           .appendText(" with the user ").appendValue(expectedAuthor)
                           .appendText(" with the text ").appendValue(expectedText)
                           .appendText(" at ").appendValue(expectedTimestamp);
            }

            @Override
            protected boolean matchesSafely(Message message, Description mismatchDescription) {
                final Captured<Id> actualId = new Captured<>();
                final Captured<User> actualAuthor = new Captured<>();
                final Captured<String> actualText = new Captured<>();
                final Captured<Instant> actualTimestamp = new Captured<>();
                message.renderTo(new MessageRenderer() {
                    @Override
                    public void render(Id id, User author, String text, Instant timestamp) {
                        actualId.set(id);
                        actualAuthor.set(author);
                        actualText.set(text);
                        actualTimestamp.set(timestamp);
                    }
                });

                mismatchDescription.appendText("was a message")
                                   .appendText(" with the ID ").appendValue(actualId.get())
                                   .appendText(" with the user ").appendValue(actualAuthor.get())
                                   .appendText(" with the text ").appendValue(actualText.get())
                                   .appendText(" at ").appendValue(actualTimestamp.get());

                return expectedId.equals(actualId.get())
                    && expectedAuthor.equals(actualAuthor.get())
                    && expectedText.equals(actualText.get())
                    && expectedTimestamp.equals(actualTimestamp.get());
            }
        };
    }
}

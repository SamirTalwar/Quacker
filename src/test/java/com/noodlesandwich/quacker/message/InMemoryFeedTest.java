package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.user.Profile;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.Test;

public class InMemoryFeedTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 4, 27, 12, 0, 0, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Feed feed = new InMemoryFeed();

    private final FeedRenderer renderer = context.mock(FeedRenderer.class);

    @Test public void
    an_empty_feed_has_no_output() {
        context.checking(new Expectations() {{
            never(renderer);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    following_a_user_results_in_a_feed_containing_quacks_from_that_user() {
        Profile profile = context.mock(Profile.class);
        feed.follow(profile);

        Message one = new Message("One.", NOW.plusSeconds(1));
        Message two = new Message("Two.", NOW.plusSeconds(2));
        Message three = new Message("Three.", NOW.plusSeconds(3));

        List<Message> messages = new ArrayList<>();
        messages.add(three);
        messages.add(two);
        messages.add(one);

        context.checking(new Expectations() {{
            allowing(profile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(messages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(three); inSequence(messages);
            oneOf(renderer).render(two); inSequence(messages);
            oneOf(renderer).render(one); inSequence(messages);
        }});
        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    private static class RenderMessages implements Action {
        private final List<Message> messages;

        public RenderMessages(List<Message> messages) {
            this.messages = messages;
        }

        @Override
        public Object invoke(Invocation invocation) throws Throwable {
            TimelineRenderer timelineRenderer = (TimelineRenderer) invocation.getParameter(0);
            for (Message message : messages) {
                timelineRenderer.render(message);
            }
            return null;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("render the messages")
                       .appendValueList("", ", ", "", messages);
        }
    }
}

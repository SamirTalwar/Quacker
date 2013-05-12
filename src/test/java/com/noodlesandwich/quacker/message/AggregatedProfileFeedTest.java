package com.noodlesandwich.quacker.message;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

import static java.time.temporal.ChronoUnit.MINUTES;

public class AggregatedProfileFeedTest {
    private static final Instant NOW = Instant.from(ZonedDateTime.of(2013, 4, 27, 12, 0, 0, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Profile myProfile = context.mock(Profile.class, "Me");
    private final Feed feed = new AggregatedProfileFeed(myProfile);

    private final FeedRenderer renderer = context.mock(FeedRenderer.class);

    @Test public void
    an_empty_feed_has_no_output() {
        context.checking(new Expectations() {{
            allowing(myProfile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(new ArrayList<>()));
            never(renderer);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    your_own_tweets_are_part_of_your_feed() {
        Message one = new Message("A B C!", NOW.plusSeconds(1));
        Message two = new Message("It's as easy as 1 2 3!", NOW.plusSeconds(2));
        Message three = new Message("As simple as Do Ray Me!", NOW.plusSeconds(3));

        Iterable<Message> messages = listOf(three, two, one);

        context.checking(new Expectations() {{
            allowing(myProfile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(messages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(three); inSequence(messages);
            oneOf(renderer).render(two); inSequence(messages);
            oneOf(renderer).render(one); inSequence(messages);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    following_a_user_results_in_a_feed_containing_quacks_from_that_user() {
        Profile profile = context.mock(Profile.class, "Other Guy");
        feed.follow(profile);

        Message one = new Message("One.", NOW.plusSeconds(1));
        Message two = new Message("Two.", NOW.plusSeconds(2));
        Message three = new Message("Three.", NOW.plusSeconds(3));

        Iterable<Message> messages = listOf(three, two, one);

        context.checking(new Expectations() {{
            allowing(myProfile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(new ArrayList<>()));
            allowing(profile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(messages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(three); inSequence(messages);
            oneOf(renderer).render(two); inSequence(messages);
            oneOf(renderer).render(one); inSequence(messages);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    following_multiple_users_results_in_a_feed_in_the_correct_order() {
        Profile bugs = context.mock(Profile.class, "Bugs");
        Profile daffy = context.mock(Profile.class, "Daffy");

        feed.follow(bugs);
        feed.follow(daffy);

        Message bugs1 = new Message("Ya got me dead to rights, doc. Do you want to shoot me now or wait 'till you get home?", NOW.plus(1, MINUTES));
        Message daffy1 = new Message("Shoot him now! Shoot him now!", NOW.plus(2, MINUTES));
        Message bugs2 = new Message("You keep out of this--he doesn't have to shoot you now!", NOW.plus(3, MINUTES));
        Message daffy2 = new Message("Well, I say that he does have to shoot me now! So shoot me now!", NOW.plus(4, MINUTES));

        Iterable<Message> bugsMessages = listOf(bugs2, bugs1);
        Iterable<Message> daffyMessages = listOf(daffy2, daffy1);

        context.checking(new Expectations() {{
            allowing(myProfile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(new ArrayList<>()));
            allowing(bugs).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(bugsMessages));
            allowing(daffy).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(daffyMessages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(daffy2); inSequence(messages);
            oneOf(renderer).render(bugs2); inSequence(messages);
            oneOf(renderer).render(daffy1); inSequence(messages);
            oneOf(renderer).render(bugs1); inSequence(messages);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    you_are_part_of_the_conversation() {
        Profile scooby = context.mock(Profile.class, "Scooby");
        Profile shaggy = context.mock(Profile.class, "Shaggy");

        feed.follow(scooby);
        feed.follow(shaggy);

        Message scooby1 = new Message("I'm hungry Shaggy.", NOW.plus(1, MINUTES));
        Message scooby2 = new Message("Got a Scooby Snack?", NOW.plus(2, MINUTES));
        Message shaggy1 = new Message("I'll give you half of my Scooby Snack.", NOW.plus(4, MINUTES));
        Message scooby3 = new Message("Yay!", NOW.plus(8, MINUTES));
        Message you1 = new Message("Put down the Scooby Snacks and no one gets hurt.", NOW.plus(16, MINUTES));
        Message shaggy2 = new Message("I already ate the Scooby Snack. Sorry, Fred.", NOW.plus(32, MINUTES));

        Iterable<Message> scoobyMessages = listOf(scooby3, scooby2, scooby1);
        Iterable<Message> shaggyMessages = listOf(shaggy2, shaggy1);
        Iterable<Message> yourMessages = listOf(you1);

        context.checking(new Expectations() {{
            allowing(myProfile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(yourMessages));
            allowing(scooby).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(scoobyMessages));
            allowing(shaggy).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(shaggyMessages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(shaggy2); inSequence(messages);
            oneOf(renderer).render(you1); inSequence(messages);
            oneOf(renderer).render(scooby3); inSequence(messages);
            oneOf(renderer).render(shaggy1); inSequence(messages);
            oneOf(renderer).render(scooby2); inSequence(messages);
            oneOf(renderer).render(scooby1); inSequence(messages);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    there_are_a_maximum_of_20_messages_in_the_feed() {
        Profile profileA = context.mock(Profile.class, "Profile A");
        Profile profileB = context.mock(Profile.class, "Profile B");
        Profile profileC = context.mock(Profile.class, "Profile C");

        feed.follow(profileA);
        feed.follow(profileB);
        feed.follow(profileC);

        List<Message> profileAMessages = new ArrayList<>();
        List<Message> profileBMessages = new ArrayList<>();
        List<Message> profileCMessages = new ArrayList<>();

        final int messageCount = 10;
        for (int messageIndex = messageCount; messageIndex > 0; --messageIndex) {
            profileAMessages.add(new Message("Message " + messageIndex + " from profile A", NOW.plus(messageIndex * 3 + 1, MINUTES)));
            profileBMessages.add(new Message("Message " + messageIndex + " from profile B", NOW.plus(messageIndex * 3 + 2, MINUTES)));
            profileCMessages.add(new Message("Message " + messageIndex + " from profile C", NOW.plus(messageIndex * 3 + 3, MINUTES)));
        }

        context.checking(new Expectations() {{
            allowing(myProfile).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(new ArrayList<>()));
            allowing(profileA).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(profileAMessages));
            allowing(profileB).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(profileBMessages));
            allowing(profileC).renderTimelineTo(with(any(TimelineRenderer.class))); will(new RenderMessages(profileCMessages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(profileCMessages.get(0)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(0)); inSequence(messages);
            oneOf(renderer).render(profileAMessages.get(0)); inSequence(messages);
            oneOf(renderer).render(profileCMessages.get(1)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(1)); inSequence(messages);
            oneOf(renderer).render(profileAMessages.get(1)); inSequence(messages);
            oneOf(renderer).render(profileCMessages.get(2)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(2)); inSequence(messages);
            oneOf(renderer).render(profileAMessages.get(2)); inSequence(messages);
            oneOf(renderer).render(profileCMessages.get(3)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(3)); inSequence(messages);
            oneOf(renderer).render(profileAMessages.get(3)); inSequence(messages);
            oneOf(renderer).render(profileCMessages.get(4)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(4)); inSequence(messages);
            oneOf(renderer).render(profileAMessages.get(4)); inSequence(messages);
            oneOf(renderer).render(profileCMessages.get(5)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(5)); inSequence(messages);
            oneOf(renderer).render(profileAMessages.get(5)); inSequence(messages);
            oneOf(renderer).render(profileCMessages.get(6)); inSequence(messages);
            oneOf(renderer).render(profileBMessages.get(6)); inSequence(messages);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    private static class RenderMessages implements Action {
        private final Iterable<Message> messages;

        public RenderMessages(Iterable<Message> messages) {
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

    @SafeVarargs
    private final <T> Iterable<T> listOf(T... items) {
        List<T> list = new ArrayList<>(items.length);
        Collections.addAll(list, items);
        return list;
    }
}

package com.noodlesandwich.quacker.communication.feed;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.User;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.Test;

import static com.noodlesandwich.quacker.communication.feed.AggregatedProfileFeedTest.RenderMessages.renderNoMessages;
import static java.time.temporal.ChronoUnit.MINUTES;

public class AggregatedProfileFeedTest {
    private static final Instant Now = Instant.from(ZonedDateTime.of(2013, 4, 27, 12, 0, 0, 0, ZoneId.of("UTC")));

    private final Mockery context = new Mockery();
    private final Profile myProfile = context.mock(Profile.class, "Me");
    private final Feed feed = new AggregatedProfileFeed(myProfile);

    private final FeedRenderer renderer = context.mock(FeedRenderer.class);

    @Test public void
    an_empty_feed_has_no_output() {
        context.checking(new Expectations() {{
            allowing(myProfile).iterator(); will(renderNoMessages());
            never(renderer);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    @Test public void
    your_own_tweets_are_part_of_your_feed() {
        User user = context.mock(User.class);
        final Message one = new Message(new Id(9), user, "A B C!", Now.plusSeconds(1));
        final Message two = new Message(new Id(10), user, "It's as easy as 1 2 3!", Now.plusSeconds(2));
        final Message three = new Message(new Id(12), user, "As simple as Do Ray Me!", Now.plusSeconds(3));

        final Iterable<Message> messages = ImmutableList.of(three, two, one);

        context.checking(new Expectations() {{
            allowing(myProfile).iterator(); will(new RenderMessages(messages));

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
        final Profile profile = context.mock(Profile.class, "Other Guy");
        feed.follow(profile);

        User user = context.mock(User.class);
        final Message one = new Message(new Id(1), user, "One.", Now.plusSeconds(1));
        final Message two = new Message(new Id(2), user, "Two.", Now.plusSeconds(2));
        final Message three = new Message(new Id(3), user, "Three.", Now.plusSeconds(3));

        final Iterable<Message> messages = ImmutableList.of(three, two, one);

        context.checking(new Expectations() {{
            allowing(myProfile).iterator(); will(renderNoMessages());
            allowing(profile).iterator(); will(new RenderMessages(messages));

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
        final Profile bugs = context.mock(Profile.class, "Bugs");
        final Profile daffy = context.mock(Profile.class, "Daffy");

        feed.follow(bugs);
        feed.follow(daffy);

        User bugsUser = context.mock(User.class, "Bugs (User)");
        User daffyUser = context.mock(User.class, "Daffy (User)");
        final Message bugs1 = new Message(new Id(50), bugsUser, "Ya got me dead to rights, doc. Do you want to shoot me now or wait 'till you get home?", Now.plus(1, MINUTES));
        final Message daffy1 = new Message(new Id(51), daffyUser, "Shoot him now! Shoot him now!", Now.plus(2, MINUTES));
        final Message bugs2 = new Message(new Id(52), bugsUser, "You keep out of this--he doesn't have to shoot you now!", Now.plus(3, MINUTES));
        final Message daffy2 = new Message(new Id(53), daffyUser, "Well, I say that he does have to shoot me now! So shoot me now!", Now.plus(4, MINUTES));

        final Iterable<Message> bugsMessages = ImmutableList.of(bugs2, bugs1);
        final Iterable<Message> daffyMessages = ImmutableList.of(daffy2, daffy1);

        context.checking(new Expectations() {{
            allowing(myProfile).iterator(); will(renderNoMessages());
            allowing(bugs).iterator(); will(new RenderMessages(bugsMessages));
            allowing(daffy).iterator(); will(new RenderMessages(daffyMessages));

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
        final Profile scooby = context.mock(Profile.class, "Scooby");
        final Profile shaggy = context.mock(Profile.class, "Shaggy");

        feed.follow(scooby);
        feed.follow(shaggy);

        User myUser = context.mock(User.class, "Me (User)");
        User scoobyUser = context.mock(User.class, "Scooby (User)");
        User shaggyUser = context.mock(User.class, "Shaggy (User)");
        final Message scooby1 = new Message(new Id(99), scoobyUser, "I'm hungry Shaggy.", Now.plus(1, MINUTES));
        final Message scooby2 = new Message(new Id(101), scoobyUser, "Got a Scooby Snack?", Now.plus(2, MINUTES));
        final Message shaggy1 = new Message(new Id(105), shaggyUser, "I'll give you half of my Scooby Snack.", Now.plus(4, MINUTES));
        final Message scooby3 = new Message(new Id(119), scoobyUser, "Yay!", Now.plus(8, MINUTES));
        final Message me1 = new Message(new Id(120), myUser, "Put down the Scooby Snacks and no one gets hurt.", Now.plus(16, MINUTES));
        final Message shaggy2 = new Message(new Id(125), shaggyUser, "I already ate the Scooby Snack. Sorry, Fred.", Now.plus(32, MINUTES));

        final Iterable<Message> scoobyMessages = ImmutableList.of(scooby3, scooby2, scooby1);
        final Iterable<Message> shaggyMessages = ImmutableList.of(shaggy2, shaggy1);
        final Iterable<Message> myMessages = ImmutableList.of(me1);

        context.checking(new Expectations() {{
            allowing(myProfile).iterator(); will(new RenderMessages(myMessages));
            allowing(scooby).iterator(); will(new RenderMessages(scoobyMessages));
            allowing(shaggy).iterator(); will(new RenderMessages(shaggyMessages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(shaggy2); inSequence(messages);
            oneOf(renderer).render(me1); inSequence(messages);
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
        final Profile profileA = context.mock(Profile.class, "Profile A");
        final Profile profileB = context.mock(Profile.class, "Profile B");
        final Profile profileC = context.mock(Profile.class, "Profile C");

        feed.follow(profileA);
        feed.follow(profileB);
        feed.follow(profileC);

        final List<Message> profileAMessages = new ArrayList<>();
        final List<Message> profileBMessages = new ArrayList<>();
        final List<Message> profileCMessages = new ArrayList<>();

        User userA = context.mock(User.class, "User A");
        User userB = context.mock(User.class, "User B");
        User userC = context.mock(User.class, "User C");
        final int messageCount = 10;
        for (int messageIndex = messageCount; messageIndex > 0; --messageIndex) {
            profileAMessages.add(new Message(new Id(messageIndex * 3 + 1), userA, "Message " + messageIndex + " from profile A", Now.plus(messageIndex * 3 + 1, MINUTES)));
            profileBMessages.add(new Message(new Id(messageIndex * 3 + 2), userB, "Message " + messageIndex + " from profile B", Now.plus(messageIndex * 3 + 2, MINUTES)));
            profileCMessages.add(new Message(new Id(messageIndex * 3 + 3), userC, "Message " + messageIndex + " from profile C", Now.plus(messageIndex * 3 + 3, MINUTES)));
        }

        context.checking(new Expectations() {{
            allowing(myProfile).iterator(); will(renderNoMessages());
            allowing(profileA).iterator(); will(new RenderMessages(profileAMessages).stoppingAfter(7));
            allowing(profileB).iterator(); will(new RenderMessages(profileBMessages).stoppingAfter(8));
            allowing(profileC).iterator(); will(new RenderMessages(profileCMessages).stoppingAfter(8));

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

    @Test public void
    users_can_block_messages_containing_certain_strings() {
        Profile profile = context.mock(Profile.class);
        User user = context.mock(User.class);

        feed.follow(profile);
        feed.block("donuts");

        final Message messageA = new Message(new Id(201), user, "I could do with some food.", Now.plus(1, MINUTES));
        final Message messageB = new Message(new Id(202), user, "How about some donuts?", Now.plus(2, MINUTES));
        final Message messageC = new Message(new Id(203), user, "Mmmm... they are delicious.", Now.plus(3, MINUTES));
        final Message messageD = new Message(new Id(204), user, "It's settled then. Donuts it is.", Now.plus(4, MINUTES));

        final List<Message> profileMessages = ImmutableList.of(messageA, messageB, messageC, messageD);

        context.checking(new Expectations() {{
            allowing(user).getUsername(); will(returnValue("Him"));

            allowing(myProfile).iterator(); will(renderNoMessages());
            allowing(profile).iterator(); will(new RenderMessages(profileMessages));

            Sequence messages = context.sequence("messages");
            oneOf(renderer).render(messageA); inSequence(messages);
            oneOf(renderer).render(messageC); inSequence(messages);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }

    public static class RenderMessages implements Action {
        private final Iterable<Message> messages;
        private int expectToStopAfter = Integer.MAX_VALUE;

        public RenderMessages(Iterable<Message> messages) {
            this.messages = messages;
        }

        public static RenderMessages renderNoMessages() {
            return new RenderMessages(ImmutableList.<Message>of());
        }

        public RenderMessages stoppingAfter(int expectation) {
            this.expectToStopAfter = expectation;
            return this;
        }

        @Override
        public Object invoke(Invocation invocation) throws Throwable {
            final Iterator<Message> iterator = messages.iterator();
            return new Iterator<Message>() {
                private int messagesRendered = 0;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Message next() {
                    if (messagesRendered == expectToStopAfter) {
                        throw new IllegalStateException("Expected to stop after iterating through " + expectToStopAfter + " messages.");
                    }
                    messagesRendered++;
                    return iterator.next();
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("render the messages")
                       .appendValueList("", ", ", "", messages);
        }
    }
}

package com.noodlesandwich.quacker.features;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.ConversationRenderer;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.users.User;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matcher;

import static com.noodlesandwich.quacker.features.InspectibleMessage.InspectibleMessageMatcher.anInspectibleMessageFrom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.contains;

public class TimelineSteps {
    private final Server server;
    private Client client;

    private final List<InspectibleMessage> messages = new ArrayList<>();
    private final MessageListRenderer messageListRenderer = new MessageListRenderer(new MessageRenderer() {
        @Override public void render(Id id, User author, String text, Instant timestamp) {
            messages.add(new InspectibleMessage(id, author, text, timestamp));
        }
    });

    @Inject
    public TimelineSteps(Server server) {
        this.server = server;
    }

    @Given("^([^ ]+) quacks \"([^\"]*)\"$") public void
    publishes(String author, String message) throws Throwable {
        client = Quacker.clientFor(server).loginAs(author);
        client.publish(message);
    }

    @Given("^a bunch of quacks from ([^ ]+)$") public void
    lots_of_quacks_from(String author) {
        client = Quacker.clientFor(server).loginAs(author);
        for (int i = 0; i < 20; ++i) {
            client.publish(aRandomMessage());
        }
    }

    @Given("the timeline:") public void
    the_timeline(List<List<String>> quacks) {
        for (List<String> quack : quacks) {
            String quacker = quack.get(0);
            String message = quack.get(1);
            Client currentClient = Quacker.clientFor(server).loginAs(quacker);
            currentClient.publish(message);
        }
    }

    @When("^([^ ]+) opens up ([^']+)'s timeline$") public void
    opens_up_a_timeline(String viewer, String timelineOwner) throws Throwable {
        client = Quacker.clientFor(server).loginAs(viewer);
        messages.clear();
        client.openTimelineOf(timelineOwner, messageListRenderer);
    }

    @When("^([^ ]+) opens up (?:his|her) feed$") public void
    opens_feed(String viewer) throws Throwable {
        client = Quacker.clientFor(server).loginAs(viewer);
        messages.clear();
        client.openFeed(messageListRenderer);
    }

    @When("^clicks on the quack from ([^ ]+) that says \"([^\"]*)\"$") public void
    clicks_on_the_quack(String author, String text) {
        for (InspectibleMessage message : messages) {
            if (author.equals(message.author.getUsername()) && text.equals(message.text)) {
                messages.clear();
                client.viewConversationAround(message.id, messageListRenderer);
                return;
            }
        }
        throw new IllegalArgumentException("No such message.");
    }

    @Then("^s?he should see:$") public void
    he_should_see(List<List<String>> quacks) throws Throwable {
        List<Matcher<? super InspectibleMessage>> matchers = new ArrayList<>();
        int lastMessageIndex = quacks.size() - 1;
        if (quacks.get(lastMessageIndex).get(0).equals("...")) {
            matchers.addAll(messagesMatching(quacks.subList(0, lastMessageIndex)));
            for (int i = lastMessageIndex; i < 20; ++i) {
                matchers.add(any(InspectibleMessage.class));
            }
        } else {
            matchers.addAll(messagesMatching(quacks));
        }
        assertThat(messages, contains(matchers));
    }

    private static List<Matcher<? super InspectibleMessage>> messagesMatching(List<List<String>> quacks) {
        List<Matcher<? super InspectibleMessage>> matchers = new ArrayList<>(quacks.size());
        for (List<String> quack : quacks) {
            String quacker = quack.get(0);
            String message = quack.get(1);
            matchers.add(anInspectibleMessageFrom(quacker).stating(message));
        }
        return matchers;
    }

    private static final int RANDOM_MESSAGE_LENGTH = 20;
    private static final char[] ALLOWED_RANDOM_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890 .,;:'!?".toCharArray();
    private static String aRandomMessage() {
        Random random = new Random();
        char[] characters = new char[RANDOM_MESSAGE_LENGTH];
        for (int i = 0; i < RANDOM_MESSAGE_LENGTH; i++) {
            characters[i] = ALLOWED_RANDOM_CHARACTERS[random.nextInt(ALLOWED_RANDOM_CHARACTERS.length)];
        }
        return String.valueOf(characters);
    }

    private class MessageListRenderer implements ConversationRenderer, FeedRenderer, TimelineRenderer {
        private final MessageRenderer messageRenderer;

        public MessageListRenderer(MessageRenderer messageRenderer) {
            this.messageRenderer = messageRenderer;
        }

        @Override
        public void render(Message message) {
            message.renderTo(messageRenderer);
        }
    }
}

package com.noodlesandwich.quacker.features;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.user.User;
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

    private final List<InspectibleMessage> messages = new ArrayList<>();
    private final MessageRenderer messageRenderer = new MessageRenderer() {
        @Override public void render(Id id, User author, String text, Instant timestamp) {
            messages.add(new InspectibleMessage(id, author, text, timestamp));
        }
    };

    @Inject
    public TimelineSteps(Server server) {
        this.server = server;
    }

    @Given("^([^ ]+) quacks \"([^\"]*)\"$") public void
    publishes(String quacker, String message) throws Throwable {
        Client client = Quacker.clientFor(server).loginAs(quacker);
        client.publish(message);
    }

    @Given("^a bunch of quacks from ([^ ]+)$") public void
    lots_of_quacks(String quacker) {
        Client client = Quacker.clientFor(server).loginAs(quacker);
        for (int i = 0; i < 20; ++i) {
            client.publish(aRandomMessage());
        }
    }

    @When("^([^ ]+) opens up ([^']+)'s timeline$") public void
    opens_up_a_timeline(String viewer, String timelineOwner) throws Throwable {
        Client client = Quacker.clientFor(server).loginAs(viewer);
        client.openTimelineOf(timelineOwner, new TimelineRenderer() {
            @Override
            public void render(Message message) {
                message.renderTo(messageRenderer);
            }
        });
    }

    @When("^([^ ]+) opens up (?:his|her) feed$") public void
    opens_feed(String viewer) throws Throwable {
        Client client = Quacker.clientFor(server).loginAs(viewer);
        client.openFeed(new FeedRenderer() {
            @Override public void render(Message message) {
                message.renderTo(messageRenderer);
            }
        });
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
}

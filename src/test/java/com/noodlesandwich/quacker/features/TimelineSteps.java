package com.noodlesandwich.quacker.features;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.message.Message;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimelineSteps {
    private final Server server;

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream outputStream = new PrintStream(output);

    private final MessageRenderer messageRenderer = new MessageRenderer() {
        @Override public void render(String text, Instant timestamp) {
            outputStream.append(text).append('\n');
        }
    };

    @Inject
    public TimelineSteps(Server server) {
        this.server = server;
    }

    @Given("^([^ ]+) quacks \"([^\"]*)\"$") public void
    publishes(String user, String message) throws Throwable {
        Client client = Quacker.clientFor(server).loginAs(user);
        client.publish(message);
    }

    @When("^([^ ]+) opens up ([^']+)'s timeline$") public void
    opens_up_a_timeline(String viewer, String timelineOwner) throws Throwable {
        Client client = Quacker.clientFor(server).loginAs(viewer);
        client.openTimelineOf(timelineOwner, new TimelineRenderer() {
            @Override public void render(Message message) {
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
    he_should_see(List<String> messages) throws Throwable {
        assertThat(output(), is(messages));
    }

    private List<String> output() {
        return Arrays.asList(output.toString().split("\n"));
    }
}

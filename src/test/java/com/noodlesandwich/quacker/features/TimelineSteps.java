package com.noodlesandwich.quacker.features;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.CommandLineUserInterface;
import com.noodlesandwich.quacker.ui.UserInterface;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimelineSteps {
    private final Server server;
    private final UserInterface cli = new CommandLineUserInterface();

    private PrintStream originalOut = null;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Inject
    public TimelineSteps(Server server) {
        this.server = server;
    }

    @Before
    public void capture_command_line() {
        originalOut = System.out;
        System.setOut(new PrintStream(output));
    }

    @After
    public void restore_command_line() {
        if (originalOut != null) {
            System.setOut(originalOut);
        }
    }

    @Given("([^ ]+) quacks \"([^\"]*)\"$")
    public void publishes(String user, String message) throws Throwable {
        Client client = Quacker.clientFor(server, cli).loginAs(user);
        client.publish(message);
    }

    @When("^([^ ]+) opens up ([^']+)'s timeline$")
    public void opens_up_a_timeline(String viewer, String timelineOwner) throws Throwable {
        Client client = Quacker.clientFor(server, cli).loginAs(viewer);
        client.openTimelineOf(timelineOwner);
    }

    @Then("^s?he should see:$")
    public void he_should_see(List<String> messages) throws Throwable {
        assertThat(output(), is(messages));
    }

    private List<String> output() {
        return Arrays.asList(output.toString().split("\n"));
    }
}

package com.noodlesandwich.quacker.features;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.noodlesandwich.quacker.Timeline;
import com.noodlesandwich.quacker.User;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimelineSteps {
    private final Map<String, User> users = new HashMap<>();

    private Timeline currentTimeline = null;
    private final ByteArrayOutputStream screen = new ByteArrayOutputStream();

    @Given("([^ ]+) quacks \"([^\"]*)\"$")
    public void publishes(String quacker, String message) throws Throwable {
        user(quacker).publish(message);
    }

    @When("^[^ ]+ opens up ([^']+)'s timeline$")
    public void opens_up_a_timeline(String timelineOwner) throws Throwable {
        currentTimeline = user(timelineOwner).timeline();
    }

    @Then("^s?he should see:$")
    public void he_should_see(List<String> messages) throws Throwable {
        currentTimeline.renderTo(screen);
        assertThat(output(), is(messages));
    }

    private User user(String name) {
        if (!users.containsKey(name)) {
            users.put(name, new User());
        }
        return users.get(name);
    }

    private List<String> output() {
        return Arrays.asList(screen.toString().split("\n"));
    }
}

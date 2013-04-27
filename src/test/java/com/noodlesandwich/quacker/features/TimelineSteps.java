package com.noodlesandwich.quacker.features;

import com.noodlesandwich.quacker.Message;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

public class TimelineSteps {
    @Given("([^ ]+) quacks \"([^\"]*)\"$")
    public void quacks(String quacker, Message message) throws Throwable {
        throw new PendingException();
    }

    @When("^([^ ]+) opens up ([^']+)'s timeline$")
    public void opens_up_a_timeline(String viewer, String timelineOwner) throws Throwable {
        throw new PendingException();
    }

    @Then("^s?he should see:$")
    public void he_should_see(List<Message> messages) throws Throwable {
        throw new PendingException();
    }
}

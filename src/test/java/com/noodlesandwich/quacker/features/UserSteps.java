package com.noodlesandwich.quacker.features;

import javax.inject.Inject;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.server.Server;
import cucumber.api.java.en.Given;

public class UserSteps {
    private final Server server;

    @Inject
    public UserSteps(Server server) {
        this.server = server;
    }

    @Given("^there is a user named ([^ ]+)$") public void
    there_is_a_user_named(String username) {
        server.registerUserNamed(username);
    }

    @Given("^([^ ]+) follows ([^ ]+)$") public void
    follows(String follower, String followee) {
        Client client = Quacker.clientFor(server).loginAs(follower);
        client.follow(followee);
    }
}

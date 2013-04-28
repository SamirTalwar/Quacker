package com.noodlesandwich.quacker.user;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class InMemoryUsersTest {
    private final Users users = new InMemoryUsers();
    @Test public void
    hands_over_a_previously_created_user() {
        users.register("Govinda");
        assertThat(users.userNamed("Govinda"), is(notNullValue()));
    }
}

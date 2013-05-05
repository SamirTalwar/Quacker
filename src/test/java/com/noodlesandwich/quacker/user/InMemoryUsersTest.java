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

    @Test public void
    hands_over_a_read_only_profile() {
        users.register("Madhuri");
        assertThat(users.profileFor("Madhuri"), is(notNullValue()));
    }

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_user_is_requested() {
        users.userNamed("Hrithik");
    }
}

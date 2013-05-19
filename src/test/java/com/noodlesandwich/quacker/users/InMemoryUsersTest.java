package com.noodlesandwich.quacker.users;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class InMemoryUsersTest {
    private final InMemoryUsers users = new InMemoryUsers();

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

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_profile_is_requested() {
        users.profileFor("Om");
    }

    @Test(expected=UserAlreadyExistsException.class) public void
    goes_boom_if_someone_tries_to_register_with_a_name_that_is_already_taken() {
        users.register("Yash");
        users.register("Yash");
    }
}

package com.noodlesandwich.quacker.user;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InMemoryUsersTest {
    private final Mockery context = new Mockery();
    private final UserFactory userFactory = context.mock(UserFactory.class);
    private final Users users = new InMemoryUsers(userFactory);

    @Test public void
    hands_over_a_previously_created_user() {
        User user = context.mock(User.class);
        context.checking(new Expectations() {{
            oneOf(userFactory).createUser(); will(returnValue(user));
        }});
        users.register("Govinda");
        assertThat(users.userNamed("Govinda"), is(user));
    }

    @Test(expected=NonExistentUserException.class) public void
    explodes_if_an_unknown_user_is_requested() {
        users.userNamed("Hrithik");
    }
}

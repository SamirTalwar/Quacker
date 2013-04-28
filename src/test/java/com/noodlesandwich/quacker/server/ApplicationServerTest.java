package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.User;
import com.noodlesandwich.quacker.user.Users;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApplicationServerTest {
    private final Mockery context = new Mockery();
    private final Users users = context.mock(Users.class);
    private final Server server = new ApplicationServer(users);

    @Test public void
    returns_an_authenticated_user() {
        User devaraj = context.mock(User.class);
        context.checking(new Expectations() {{
            oneOf(users).userNamed("Devaraj"); will(returnValue(devaraj));
        }});
        assertThat(server.authenticatedUserNamed("Devaraj"), is(devaraj));
    }
}

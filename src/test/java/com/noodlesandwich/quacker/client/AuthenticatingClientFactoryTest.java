package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.users.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AuthenticatingClientFactoryTest {
    private final Mockery context = new Mockery();

    private final ClientFactory clientFactory = new AuthenticatingClientFactory();

    @Test public void
    authenticates_with_the_server() {
        final Server server = context.mock(Server.class);
        final User user = context.mock(User.class);

        context.checking(new Expectations() {{
            oneOf(server).authenticatedUserNamed("Bharat"); will(returnValue(user));
        }});

        assertThat(clientFactory.newClient(server, "Bharat"), is(notNullValue()));

        context.assertIsSatisfied();
    }
}

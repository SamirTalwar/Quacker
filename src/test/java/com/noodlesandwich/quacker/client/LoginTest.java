package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LoginTest {
    private final Mockery context = new Mockery();
    private final Server server = context.mock(Server.class);
    private final ClientFactory clientFactory = context.mock(ClientFactory.class);
    private final Login login = new Login(server, clientFactory);

    @Test public void
    authenticates_with_the_server() {
        final Client client = context.mock(Client.class);
        context.checking(new Expectations() {{
            oneOf(clientFactory).newClient(server, "Anil"); will(returnValue(client));
        }});

        assertThat(login.loginAs("Anil"), is(client));

        context.assertIsSatisfied();
    }
}

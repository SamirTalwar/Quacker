package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.CommunicationChannel;
import com.noodlesandwich.quacker.server.Server;
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
        Server server = context.mock(Server.class);

        CommunicationChannel communicationChannel = context.mock(CommunicationChannel.class);
        context.checking(new Expectations() {{
            oneOf(server).authenticatedClientFor("Bharat"); will(returnValue(communicationChannel));
        }});

        assertThat(clientFactory.newClient(server, "Bharat"), is(notNullValue()));

        context.assertIsSatisfied();
    }
}

package com.noodlesandwich.quacker.client;

import java.time.Clock;
import javax.inject.Inject;
import com.noodlesandwich.quacker.server.Server;

public class AuthenticatingClientFactory implements ClientFactory {
    private final Clock clock;

    @Inject
    public AuthenticatingClientFactory(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Client newClient(Server server, String username) {
        return new AuthenticatedClient(clock, server.authenticatedUserNamed(username), new ProfileDownloader(server));
    }
}

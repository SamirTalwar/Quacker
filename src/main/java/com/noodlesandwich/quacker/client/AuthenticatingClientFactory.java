package com.noodlesandwich.quacker.client;

import java.time.Clock;
import javax.inject.Inject;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.UserInterface;

public class AuthenticatingClientFactory implements ClientFactory {
    private final Clock clock;
    private final UserInterface userInterface;

    @Inject
    public AuthenticatingClientFactory(Clock clock, UserInterface userInterface) {
        this.clock = clock;
        this.userInterface = userInterface;
    }

    @Override
    public Client newClient(Server server, String username) {
        return new AuthenticatedClient(clock, userInterface, server.authenticatedUserNamed(username), new ProfileDownloader(server));
    }
}

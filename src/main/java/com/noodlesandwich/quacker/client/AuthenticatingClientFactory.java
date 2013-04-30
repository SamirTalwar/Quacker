package com.noodlesandwich.quacker.client;

import javax.inject.Inject;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.UserInterface;

public class AuthenticatingClientFactory implements ClientFactory {
    private final UserInterface userInterface;

    @Inject
    public AuthenticatingClientFactory(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public Client newClient(Server server, String username) {
        return new AuthenticatedClient(userInterface, server.authenticatedUserNamed(username), new ProfileDownloader(server));
    }
}

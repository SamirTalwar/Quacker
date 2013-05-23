package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;

public class AuthenticatingClientFactory implements ClientFactory {
    @Override
    public Client newClient(Server server, String username) {
        return new AuthenticatedClient(
                server.authenticatedUserNamed(username),
                new ProfileDownloader(server),
                new ConversationDownloader(server));
    }
}

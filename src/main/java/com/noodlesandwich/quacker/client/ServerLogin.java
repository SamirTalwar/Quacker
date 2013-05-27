package com.noodlesandwich.quacker.client;

import javax.inject.Inject;
import com.noodlesandwich.quacker.server.Server;

public class ServerLogin implements Login {
    private final Server server;
    private final ClientFactory clientFactory;

    @Inject
    public ServerLogin(Server server, ClientFactory clientFactory) {
        this.server = server;
        this.clientFactory = clientFactory;
    }

    @Override
    public Client loginAs(String username) {
        return clientFactory.newClient(server, username);
    }
}

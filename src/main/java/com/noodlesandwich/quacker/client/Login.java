package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;

public class Login {
    private final Server server;
    private final ClientFactory clientFactory;

    public Login(Server server, ClientFactory clientFactory) {
        this.server = server;
        this.clientFactory = clientFactory;
    }

    public Client loginAs(String username) {
        return clientFactory.newClient(server, username);
    }
}

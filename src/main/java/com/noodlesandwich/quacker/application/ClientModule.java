package com.noodlesandwich.quacker.application;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.noodlesandwich.quacker.client.AuthenticatingClientFactory;
import com.noodlesandwich.quacker.client.ClientFactory;
import com.noodlesandwich.quacker.server.Server;

public class ClientModule implements Module {
    private final Server server;

    public ClientModule(Server server) {
        this.server = server;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Server.class).toInstance(server);
        binder.bind(ClientFactory.class).to(AuthenticatingClientFactory.class);
    }
}

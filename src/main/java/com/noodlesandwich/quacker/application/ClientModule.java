package com.noodlesandwich.quacker.application;

import java.time.Clock;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.noodlesandwich.quacker.client.AuthenticatingClientFactory;
import com.noodlesandwich.quacker.client.ClientFactory;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.UserInterface;

public class ClientModule implements Module {
    private final Server server;
    private final UserInterface userInterface;

    public ClientModule(Server server, UserInterface userInterface) {
        this.server = server;
        this.userInterface = userInterface;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Clock.class).toInstance(Clock.systemDefaultZone());

        binder.bind(Server.class).toInstance(server);
        binder.bind(UserInterface.class).toInstance(userInterface);
        binder.bind(ClientFactory.class).to(AuthenticatingClientFactory.class);
    }
}

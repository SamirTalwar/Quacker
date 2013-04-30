package com.noodlesandwich.quacker.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.noodlesandwich.quacker.client.Login;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.UserInterface;

public class Quacker {
    public static Server server() {
        Injector injector = Guice.createInjector(new ServerModule());
        return injector.getInstance(Server.class);
    }

    public static Login clientFor(Server server, UserInterface userInterface) {
        Injector injector = Guice.createInjector(new ClientModule(server, userInterface));
        return injector.getInstance(Login.class);
    }

}

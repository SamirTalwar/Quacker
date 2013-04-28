package com.noodlesandwich.quacker;

import com.noodlesandwich.quacker.client.AuthenticatingClientFactory;
import com.noodlesandwich.quacker.client.Login;
import com.noodlesandwich.quacker.server.ApplicationServer;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.user.InMemoryUsers;

public class Quacker {
    public static Server server() {
        return new ApplicationServer(new InMemoryUsers());
    }

    public static Login clientFor(Server server) {
        return new Login(server, new AuthenticatingClientFactory());
    }
}

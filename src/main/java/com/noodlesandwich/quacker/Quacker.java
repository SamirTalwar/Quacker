package com.noodlesandwich.quacker;

import com.noodlesandwich.quacker.client.Login;
import com.noodlesandwich.quacker.server.ApplicationServer;
import com.noodlesandwich.quacker.server.Server;

public class Quacker {
    public static Server server() {
        return new ApplicationServer();
    }

    public static Login clientFor(Server server) {
        throw new UnsupportedOperationException();
    }
}

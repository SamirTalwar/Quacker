package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;

public interface ClientFactory {
    Client newClient(Server server, String username);
}

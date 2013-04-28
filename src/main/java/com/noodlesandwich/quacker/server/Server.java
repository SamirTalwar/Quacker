package com.noodlesandwich.quacker.server;

public interface Server {
    CommunicationChannel authenticatedClientFor(String username);
}

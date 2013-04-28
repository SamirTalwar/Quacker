package com.noodlesandwich.quacker.server;

public class ApplicationServer implements Server {
    @Override
    public CommunicationChannel authenticatedClientFor(String username) {
        throw new UnsupportedOperationException();
    }
}

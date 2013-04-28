package com.noodlesandwich.quacker;

import com.noodlesandwich.quacker.ui.Client;
import com.noodlesandwich.quacker.ui.ClientFactory;
import com.noodlesandwich.quacker.ui.CommandLineClientFactory;

public class Quacker {
    public static Quacker quacker() {
        return new Quacker(new CommandLineClientFactory());
    }

    public Quacker(ClientFactory clientFactory) {
    }

    public Client clientFor(String username) {
        throw new UnsupportedOperationException();
    }
}

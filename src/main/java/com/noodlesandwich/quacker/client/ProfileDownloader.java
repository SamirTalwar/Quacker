package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.Profiles;

public class ProfileDownloader implements Profiles {
    private final Server server;

    public ProfileDownloader(Server server) {
        this.server = server;
    }

    @Override
    public Profile profileFor(String username) {
        return server.profileFor(username);
    }
}

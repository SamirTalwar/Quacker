package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.user.Profile;
import com.noodlesandwich.quacker.user.Profiles;

public class ProfileDownloader implements Profiles {
    private final Server server;

    public ProfileDownloader(Server server) {
        this.server = server;
    }

    @Override
    public Profile forUser(String username) {
        return server.profileFor(username);
    }
}

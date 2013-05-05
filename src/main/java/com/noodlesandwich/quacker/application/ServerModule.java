package com.noodlesandwich.quacker.application;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.noodlesandwich.quacker.server.ApplicationServer;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.user.InMemoryUsers;
import com.noodlesandwich.quacker.user.Profiles;
import com.noodlesandwich.quacker.user.Users;

public class ServerModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Server.class).to(ApplicationServer.class);
        binder.bind(Profiles.class).to(InMemoryUsers.class);
        binder.bind(Users.class).to(InMemoryUsers.class);
    }
}

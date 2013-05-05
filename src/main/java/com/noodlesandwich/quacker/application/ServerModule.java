package com.noodlesandwich.quacker.application;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.noodlesandwich.quacker.message.InMemoryTimeline;
import com.noodlesandwich.quacker.message.Timeline;
import com.noodlesandwich.quacker.message.UpdatableTimeline;
import com.noodlesandwich.quacker.server.ApplicationServer;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.user.InMemoryUser;
import com.noodlesandwich.quacker.user.InMemoryUserFactory;
import com.noodlesandwich.quacker.user.InMemoryUsers;
import com.noodlesandwich.quacker.user.User;
import com.noodlesandwich.quacker.user.UserFactory;
import com.noodlesandwich.quacker.user.Users;

public class ServerModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Server.class).to(ApplicationServer.class);

        binder.bind(UserFactory.class).annotatedWith(InMemory.class).to(InMemoryUserFactory.class);
        binder.bind(Users.class).annotatedWith(InMemory.class).to(InMemoryUsers.class);
        binder.bind(User.class).annotatedWith(InMemory.class).to(InMemoryUser.class);
        binder.bind(Timeline.class).annotatedWith(InMemory.class).to(InMemoryTimeline.class);
        binder.bind(UpdatableTimeline.class).annotatedWith(InMemory.class).to(InMemoryTimeline.class);
    }
}

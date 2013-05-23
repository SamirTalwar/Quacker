package com.noodlesandwich.quacker.application;

import java.time.Clock;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.noodlesandwich.quacker.communication.conversations.ConversationGraph;
import com.noodlesandwich.quacker.communication.conversations.Conversations;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.id.IdentifierSource;
import com.noodlesandwich.quacker.id.IncrementingIdentifierSource;
import com.noodlesandwich.quacker.server.ApplicationServer;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.users.InMemoryUsers;
import com.noodlesandwich.quacker.users.Profiles;
import com.noodlesandwich.quacker.users.Users;

public class ServerModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Clock.class).toInstance(Clock.systemDefaultZone());
        binder.bind(IdentifierSource.class).to(IncrementingIdentifierSource.class);

        binder.bind(Server.class).to(ApplicationServer.class);
        binder.bind(Users.class).to(InMemoryUsers.class);
        binder.bind(Profiles.class).to(InMemoryUsers.class);
        binder.bind(Conversations.class).to(ConversationGraph.class);
        binder.bind(MessageListener.class).to(ConversationGraph.class);
    }
}

package com.noodlesandwich.quacker.client;

import java.time.Clock;
import javax.inject.Inject;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.id.IdentifierSource;
import com.noodlesandwich.quacker.message.Conversation;
import com.noodlesandwich.quacker.message.Conversations;
import com.noodlesandwich.quacker.server.Server;

public class AuthenticatingClientFactory implements ClientFactory {
    private final Clock clock;
    private final IdentifierSource idSource;

    @Inject
    public AuthenticatingClientFactory(Clock clock, IdentifierSource idSource) {
        this.clock = clock;
        this.idSource = idSource;
    }

    @Override
    public Client newClient(Server server, String username) {
        return new AuthenticatedClient(clock, idSource, server.authenticatedUserNamed(username), new ProfileDownloader(server), new Conversations() {
            @Override public Conversation conversationAround(Id messageId) {
                throw new UnsupportedOperationException();
            }
        });
    }
}

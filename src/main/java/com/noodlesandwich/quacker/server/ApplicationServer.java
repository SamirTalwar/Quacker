package com.noodlesandwich.quacker.server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.communication.conversations.Conversations;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.Profiles;
import com.noodlesandwich.quacker.users.User;
import com.noodlesandwich.quacker.users.Users;

@Singleton
public class ApplicationServer implements Server, Remote {
    private final Users users;
    private final Profiles profiles;
    private final Conversations conversations;

    public static void main(String[] args) throws Exception {
        final Server server = Quacker.server();

        Remote stub = UnicastRemoteObject.exportObject((Remote) server, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.bind("Server", stub);

        System.err.println("Server ready");

        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    server.quit();
                }
            });
            synchronized (server) {
                server.wait();
            }
        } finally {
            System.err.println("Shutting down server...");
            registry.unbind("Server");
        }
    }

    @Inject
    public ApplicationServer(Users users, Profiles profiles, Conversations conversations) {
        this.users = users;
        this.profiles = profiles;
        this.conversations = conversations;
    }

    @Override
    public void registerUserNamed(String username) {
        users.register(username);
    }

    @Override
    public User authenticatedUserNamed(String username) {
        return users.userNamed(username);
    }

    @Override
    public Profile profileFor(String username) {
        return profiles.profileFor(username);
    }

    @Override
    public Conversation conversationAround(Id messageId) {
        return conversations.conversationAround(messageId);
    }

    @Override
    public void quit() {
        synchronized (this) {
            notify();
        }
    }
}

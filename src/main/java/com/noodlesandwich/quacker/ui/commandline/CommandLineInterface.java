package com.noodlesandwich.quacker.ui.commandline;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Instant;
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.client.Login;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.ui.MessageListRenderer;
import com.noodlesandwich.quacker.ui.MessageRenderer;
import com.noodlesandwich.quacker.users.User;

public class CommandLineInterface {
    public static void main(String[] args) throws Exception {
        String host = (args.length < 1) ? null : args[0];
        Registry registry = LocateRegistry.getRegistry(host);
        Server server = (Server) registry.lookup("Server");

        CommandLineInterface cli = new CommandLineInterface(new RealCommandLine(), Quacker.clientFor(server));
        cli.run();
    }

    private final CommandLine commandLine;
    private final Login login;
    private Client client;

    private State state = State.LoginPrompt;
    private MessageRenderer messageRenderer = new CommandLineMessageRenderer();

    public CommandLineInterface(CommandLine commandLine, Login login) {
        this.commandLine = commandLine;
        this.login = login;
    }

    public void run() throws IOException {
        while (state != State.Done) {
            state = next();
        }
    }

    private State next() throws IOException {
        switch (state) {
            case LoginPrompt:
                commandLine.write("Login: ");
                return State.LoginAction;
            case LoginAction:
                String username = commandLine.read();
                client = login.loginAs(username);
                commandLine.writeLine("Logged in successfully.");
                prompt();
                return State.LoggedIn;
            case LoggedIn:
                String command = commandLine.read();
                switch (command.charAt(0)) {
                    case 'p':
                        String message = command.substring(2);
                        client.publish(message);
                        break;
                    case 't':
                        String usernameToLookup = command.substring(2);
                        client.openTimelineOf(usernameToLookup, new MessageListRenderer(messageRenderer));
                        break;
                    case 'q':
                        return State.Done;
                }
                prompt();
                return state;
            default:
                throw new AssertionError("This should never happen.");
        }
    }

    private void prompt() throws IOException {
        commandLine.write("> ");
    }

    public class CommandLineMessageRenderer implements MessageRenderer {
        @Override public void render(Id id, User author, String text, Instant timestamp) {
            try {
                commandLine.writeLine(id + " " + author.getUsername() + ": " + text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

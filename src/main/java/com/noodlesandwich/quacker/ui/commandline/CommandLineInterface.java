package com.noodlesandwich.quacker.ui.commandline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
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

        CommandLineInterface cli = new CommandLineInterface(Quacker.clientFor(server), new InputStreamReader(System.in), new OutputStreamWriter(System.out));
        cli.run();
    }

    private final Login login;
    private Client client;

    private final BufferedReader in;
    private final BufferedWriter out;

    private State state;
    private MessageRenderer messageRenderer;

    public CommandLineInterface(Login login, Reader in, Writer out) {
        this.login = login;
        this.in = new BufferedReader(in);
        this.out = new BufferedWriter(out);
        state = State.LoginPrompt;
        messageRenderer = new CommandLineMessageRenderer();
    }

    public void run() throws IOException {
        while (next())
            ;
    }

    public boolean next() throws IOException {
        switch (state) {
            case LoginPrompt:
                write("Login: ");
                state = State.LoginAction;
                break;
            case LoginAction:
                String username = read();
                client = login.loginAs(username);
                writeLine("Logged in successfully.");
                state = State.LoggedIn;
                prompt();
                break;
            case LoggedIn:
                String command = read();
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
                        return false;
                }
                prompt();
                break;
        }

        return true;
    }

    private void prompt() throws IOException {
        write("> ");
    }

    private String read() throws IOException {
        return in.readLine();
    }

    private void write(String string) throws IOException {
        out.write(string);
        out.flush();
    }

    private void writeLine(String string) throws IOException {
        write(string);
        out.newLine();
        out.flush();
    }

    public class CommandLineMessageRenderer implements MessageRenderer {
        @Override public void render(Id id, User author, String text, Instant timestamp) {
            try {
                writeLine(id + " " + author.getUsername() + ": " + text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

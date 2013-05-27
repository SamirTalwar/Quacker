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
import com.noodlesandwich.quacker.application.Quacker;
import com.noodlesandwich.quacker.client.Client;
import com.noodlesandwich.quacker.client.Login;
import com.noodlesandwich.quacker.server.Server;

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

    public CommandLineInterface(Login login, Reader in, Writer out) {
        this.login = login;
        this.in = new BufferedReader(in);
        this.out = new BufferedWriter(out);
        state = State.LoginPrompt;
    }

    public void run() throws IOException {
        next();
    }

    public void next() throws IOException {
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
                String message = command.substring(2);
                client.publish(message);
                break;
        }
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
}

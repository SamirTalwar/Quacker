package com.noodlesandwich.quacker.ui;

import java.io.PrintStream;
import com.noodlesandwich.quacker.message.Message;

public class CommandLineUserInterface implements UserInterface {
    private final PrintStream printStream;

    public CommandLineUserInterface(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void render(Message message) {
        message.renderTo(new MessageRenderer() {
            @Override public void render(String text) {
                printStream.println(text);
            }
        });
    }
}

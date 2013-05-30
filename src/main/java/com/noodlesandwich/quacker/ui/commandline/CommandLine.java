package com.noodlesandwich.quacker.ui.commandline;

import java.io.IOException;

public interface CommandLine {
    String read() throws IOException;

    void write(String string) throws IOException;

    void writeLine(String string) throws IOException;
}

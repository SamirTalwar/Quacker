package com.noodlesandwich.quacker.ui.commandline;

import java.io.*;

public class RealCommandLine implements CommandLine {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

    @Override
    public String read() throws IOException {
        return in.readLine();
    }

    @Override
    public void write(String string) throws IOException {
        out.write(string);
        out.flush();
    }

    @Override
    public void writeLine(String string) throws IOException {
        write(string);
        out.newLine();
        out.flush();
    }
}

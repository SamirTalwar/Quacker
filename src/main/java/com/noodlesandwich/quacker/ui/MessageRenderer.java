package com.noodlesandwich.quacker.ui;

import java.time.Instant;

public interface MessageRenderer {
    void render(int id, String text, Instant timestamp);
}

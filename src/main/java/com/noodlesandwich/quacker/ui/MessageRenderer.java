package com.noodlesandwich.quacker.ui;

import java.time.Instant;

public interface MessageRenderer {
    void render(String text, Instant timestamp);
}

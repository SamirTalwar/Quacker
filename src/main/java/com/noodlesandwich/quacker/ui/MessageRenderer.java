package com.noodlesandwich.quacker.ui;

import java.time.Instant;
import com.noodlesandwich.quacker.id.Id;

public interface MessageRenderer {
    void render(Id id, String text, Instant timestamp);
}

package com.noodlesandwich.quacker.message;

import java.time.Instant;
import com.noodlesandwich.quacker.ui.MessageRenderer;

public class Message {
    private final String text;
    private final Instant timestamp;

    public Message(String text, Instant timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public void renderTo(MessageRenderer renderer) {
        renderer.render(text, timestamp);
    }
}

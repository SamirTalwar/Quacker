package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.MessageRenderer;

public class Message {
    private final String text;

    public Message(String text) {
        this.text = text;
    }

    public void renderTo(MessageRenderer renderer) {
        renderer.render(text);
    }
}

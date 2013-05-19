package com.noodlesandwich.quacker.communication.messages;

import java.time.Instant;

public class MessageTooLongException extends RuntimeException {
    public MessageTooLongException(String text, Instant timestamp) {
        super(text + " @ " + timestamp);
    }
}

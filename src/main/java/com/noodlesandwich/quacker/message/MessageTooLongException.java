package com.noodlesandwich.quacker.message;

import java.time.Instant;

public class MessageTooLongException extends RuntimeException {
    public MessageTooLongException(String text, Instant timestamp) {
        super(text + " @ " + timestamp);
    }
}

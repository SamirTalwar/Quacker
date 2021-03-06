package com.noodlesandwich.quacker.communication.messages;

import java.time.Instant;

public class EmptyMessageException extends RuntimeException {
    public EmptyMessageException(Instant timestamp) {
        super("Empty message @ " + timestamp.toString());
    }
}

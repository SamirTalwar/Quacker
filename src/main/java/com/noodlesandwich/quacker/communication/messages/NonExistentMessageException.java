package com.noodlesandwich.quacker.communication.messages;

import com.noodlesandwich.quacker.id.Id;

public class NonExistentMessageException extends RuntimeException {
    public NonExistentMessageException(Id messageId) {
        super("No such message with ID " + messageId);
    }
}

package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;

public class InMemoryUser implements User {
    @Override
    public void publish(Message message) {
        throw new UnsupportedOperationException();
    }
}

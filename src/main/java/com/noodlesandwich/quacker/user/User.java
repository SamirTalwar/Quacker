package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.message.Message;

public interface User {
    void publish(Message message);
}

package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.User;

public interface Server {
    void registerUserNamed(String username);
    User authenticatedUserNamed(String username);
}

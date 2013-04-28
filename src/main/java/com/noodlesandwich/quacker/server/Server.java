package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.User;

public interface Server {
    User authenticatedUserNamed(String username);
}

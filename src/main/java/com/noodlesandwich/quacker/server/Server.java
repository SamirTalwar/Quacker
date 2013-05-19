package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.User;

public interface Server {
    void registerUserNamed(String username);
    User authenticatedUserNamed(String username);
    Profile profileFor(String username);
}

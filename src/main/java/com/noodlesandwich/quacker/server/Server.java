package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.Profile;
import com.noodlesandwich.quacker.user.User;

public interface Server {
    void registerUserNamed(String username);
    User authenticatedUserNamed(String username);
    Profile profileFor(String username);
}

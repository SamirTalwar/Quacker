package com.noodlesandwich.quacker.user;

public interface Users {
    void register(String username);
    User userNamed(String username);
}

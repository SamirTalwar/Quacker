package com.noodlesandwich.quacker.users;

public interface Users {
    void register(String username);
    User userNamed(String username);
}

package com.noodlesandwich.quacker.user;

public class InMemoryUsers implements Users {
    @Override
    public User userNamed(String username) {
        throw new UnsupportedOperationException();
    }
}

package com.noodlesandwich.quacker.user;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User already exists: " + username);
    }
}

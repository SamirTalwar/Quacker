package com.noodlesandwich.quacker.user;

public class NonExistentUserException extends RuntimeException {
    public NonExistentUserException(String username) {
        super("No such user: " + username);
    }
}

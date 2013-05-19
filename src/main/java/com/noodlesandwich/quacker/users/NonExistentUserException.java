package com.noodlesandwich.quacker.users;

public class NonExistentUserException extends RuntimeException {
    public NonExistentUserException(String username) {
        super("No such user: " + username);
    }
}

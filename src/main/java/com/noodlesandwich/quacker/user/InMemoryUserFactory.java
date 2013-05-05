package com.noodlesandwich.quacker.user;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.noodlesandwich.quacker.application.InMemory;

public class InMemoryUserFactory implements UserFactory {
    private final Injector injector;

    @Inject
    public InMemoryUserFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public User createUser() {
        return injector.getInstance(Key.get(User.class, InMemory.class));
    }
}

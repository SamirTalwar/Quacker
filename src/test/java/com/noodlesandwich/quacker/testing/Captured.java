package com.noodlesandwich.quacker.testing;

public class Captured<T> {
    private T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}

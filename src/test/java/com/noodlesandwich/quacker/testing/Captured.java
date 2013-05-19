package com.noodlesandwich.quacker.testing;

public class Captured<T> {
    private T value;
    private boolean isCaptured = false;

    public T get() {
        if (!isCaptured) {
            throw new IllegalStateException("Value was never captured.");
        }

        return value;
    }

    public void set(T value) {
        if (isCaptured) {
            throw new IllegalStateException("Tried to capture " + value + ", but already captured once with value " + this.value + ".");
        }

        this.value = value;
        this.isCaptured = true;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    @Override
    public String toString() {
        if (!isCaptured) {
            return "<not captured>";
        }

        return value.toString();
    }

}

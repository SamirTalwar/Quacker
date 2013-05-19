package com.noodlesandwich.quacker.id;

public class Id {
    private final int value;

    public Id(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Id)) {
            return false;
        }

        Id id = (Id) o;
        return value == id.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
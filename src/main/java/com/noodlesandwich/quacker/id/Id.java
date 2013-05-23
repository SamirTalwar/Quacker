package com.noodlesandwich.quacker.id;

public class Id implements Comparable<Id> {
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

    @Override
    public int compareTo(Id other) {
        return Integer.compare(value, other.value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

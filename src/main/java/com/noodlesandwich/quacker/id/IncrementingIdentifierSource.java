package com.noodlesandwich.quacker.id;

public class IncrementingIdentifierSource implements IdentifierSource {
    private int nextInteger = 0;

    @Override
    public int nextId() {
        return ++nextInteger;
    }
}

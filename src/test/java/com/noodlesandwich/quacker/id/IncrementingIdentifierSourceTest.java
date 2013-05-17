package com.noodlesandwich.quacker.id;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IncrementingIdentifierSourceTest {
    private final IdentifierSource idSource = new IncrementingIdentifierSource();

    @Test public void
    starts_at_1() {
        assertThat(idSource.nextId().getValue(), is(1));
    }

    @Test public void
    increments_by_one_each_time() {
        assertThat(idSource.nextId().getValue(), is(1));
        assertThat(idSource.nextId().getValue(), is(2));
        assertThat(idSource.nextId().getValue(), is(3));
        assertThat(idSource.nextId().getValue(), is(4));
        assertThat(idSource.nextId().getValue(), is(5));
    }
}

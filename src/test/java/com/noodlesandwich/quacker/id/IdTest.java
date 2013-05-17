package com.noodlesandwich.quacker.id;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class IdTest {
    @Test public void
    returns_its_value() {
        Id id = new Id(42);
        assertThat(id.getValue(), is(42));
    }

    @Test public void
    is_equal_to_another_ID_with_the_same_value() {
        Id one = new Id(99);
        Id two = new Id(99);
        assertThat(one, is(equalTo(two)));
    }

    @Test public void
    is_not_equal_to_another_ID_with_a_different_value() {
        Id one = new Id(99);
        Id two = new Id(101);
        assertThat(one, is(not(equalTo(two))));
    }
}

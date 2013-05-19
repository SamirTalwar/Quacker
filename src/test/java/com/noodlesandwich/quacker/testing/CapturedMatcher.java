package com.noodlesandwich.quacker.testing;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class CapturedMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Captured<T> captured;

    CapturedMatcher(Captured<T> captured) {
        this.captured = captured;
    }

    public static <T> Matcher<T> theCaptured(final Captured<T> captured) {
        return new CapturedMatcher<>(captured);
    }

    @Override
    public void describeTo(Description description) {
        if (!captured.isCaptured()) {
            description.appendText("this will fail, as no value has been captured");
            return;
        }
        description.appendText("the captured value ").appendValue(captured);
    }

    @Override
    protected boolean matchesSafely(T value, Description mismatchDescription) {
        mismatchDescription.appendText("was the value ").appendValue(value);
        if (!captured.isCaptured()) {
            return false;
        }
        return captured.get().equals(value);
    }
}

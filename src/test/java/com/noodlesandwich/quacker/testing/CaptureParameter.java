package com.noodlesandwich.quacker.testing;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class CaptureParameter<T> implements Action {
    private final int parameterIndex;
    private final Captured<T> captured;

    public CaptureParameter(int parameterIndex, Captured<T> captured) {
        this.parameterIndex = parameterIndex;
        this.captured = captured;
    }

    public static CaptureParameterBuilder captureParameter(int parameterIndex) {
        return new CaptureParameterBuilder(parameterIndex);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Void invoke(Invocation invocation) throws Throwable {
        captured.set((T) invocation.getParameter(parameterIndex));
        return null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("capture parameter ").appendValue(parameterIndex);
    }

    public static class CaptureParameterBuilder {
        private final int parameterIndex;

        public CaptureParameterBuilder(int parameterIndex) {
            this.parameterIndex = parameterIndex;
        }

        public <T> CaptureParameter<T> as(Captured<T> captured) {
            return new CaptureParameter<>(parameterIndex, captured);
        }
    }
}

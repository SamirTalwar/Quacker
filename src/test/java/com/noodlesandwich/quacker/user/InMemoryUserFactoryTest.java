package com.noodlesandwich.quacker.user;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.noodlesandwich.quacker.application.InMemory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InMemoryUserFactoryTest {
    private final Mockery context = new Mockery();
    private final Injector injector = context.mock(Injector.class);
    private final UserFactory userFactory = new InMemoryUserFactory(injector);

    @Test public void
    creates_a_new_user() {
        User user = context.mock(User.class);
        context.checking(new Expectations() {{
            oneOf(injector).getInstance(Key.get(User.class, InMemory.class)); will(returnValue(user));
        }});

        assertThat(userFactory.createUser(), is(user));
    }
}

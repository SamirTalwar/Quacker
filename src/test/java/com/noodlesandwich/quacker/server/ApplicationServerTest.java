package com.noodlesandwich.quacker.server;

import com.noodlesandwich.quacker.user.Profile;
import com.noodlesandwich.quacker.user.Profiles;
import com.noodlesandwich.quacker.user.User;
import com.noodlesandwich.quacker.user.Users;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApplicationServerTest {
    private final Mockery context = new Mockery();
    private final Users users = context.mock(Users.class);
    private final Profiles profiles = context.mock(Profiles.class);
    private final Server server = new ApplicationServer(users, profiles);

    @Test public void
    returns_an_authenticated_user() {
        final User devaraj = context.mock(User.class);
        context.checking(new Expectations() {{
            oneOf(users).userNamed("Devaraj"); will(returnValue(devaraj));
        }});

        assertThat(server.authenticatedUserNamed("Devaraj"), is(devaraj));
        context.assertIsSatisfied();
    }

    @Test public void
    registers_a_new_user() {
        context.checking(new Expectations() {{
            oneOf(users).register("Irrfan");
        }});

        server.registerUserNamed("Irrfan");
        context.assertIsSatisfied();
    }

    @Test public void
    provides_a_read_only_profile() {
        final Profile lakshmi = context.mock(Profile.class);
        context.checking(new Expectations() {{
            oneOf(profiles).profileFor("Lakshmi"); will(returnValue(lakshmi));
        }});

        assertThat(server.profileFor("Lakshmi"), is(lakshmi));
        context.assertIsSatisfied();
    }
}

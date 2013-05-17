package com.noodlesandwich.quacker.client;

import com.noodlesandwich.quacker.server.Server;
import com.noodlesandwich.quacker.user.Profile;
import com.noodlesandwich.quacker.user.Profiles;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProfileDownloaderTest {
    private final Mockery context = new Mockery();
    private final Server server = context.mock(Server.class);
    private final Profiles profiles = new ProfileDownloader(server);

    @Test public void
    downloads_timelines_from_the_server() {
        final Profile profile = context.mock(Profile.class);

        context.checking(new Expectations() {{
            oneOf(server).profileFor("Karishma"); will(returnValue(profile));
        }});

        assertThat(profiles.profileFor("Karishma"), is(profile));
        context.assertIsSatisfied();
    }
}

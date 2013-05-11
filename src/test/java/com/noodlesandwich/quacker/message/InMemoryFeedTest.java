package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.FeedRenderer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class InMemoryFeedTest {
    private final Mockery context = new Mockery();
    private final Feed feed = new InMemoryFeed();

    @Test public void
    an_empty_feed_has_no_output() {
        FeedRenderer renderer = context.mock(FeedRenderer.class);
        context.checking(new Expectations() {{
            never(renderer);
        }});

        feed.renderTo(renderer);

        context.assertIsSatisfied();
    }
}

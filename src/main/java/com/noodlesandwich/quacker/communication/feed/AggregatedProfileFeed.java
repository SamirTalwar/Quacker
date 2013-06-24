package com.noodlesandwich.quacker.communication.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.blogspot.nurkiewicz.lazyseq.LazySeq;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.users.Profile;

public class AggregatedProfileFeed implements Feed {
    public static final Comparator<Message> MessageComparator = Collections.reverseOrder();

    private final List<Profile> followees = new ArrayList<>();
    private final Set<String> blockedStrings = new HashSet<>();

    public AggregatedProfileFeed(Profile userProfile) {
        followees.add(userProfile);
    }

    @Override
    public void follow(Profile profile) {
        this.followees.add(profile);
    }

    @Override
    public void block(String string) {
        blockedStrings.add(string);
    }

    @Override
    public void renderTo(FeedRenderer renderer) {
        LazySeq<LazySeq<Message>> timelines = LazySeq.of(followees)
                .map(LazySeq::of)
                .map(timeline -> timeline.filter(message -> !message.isBlockedByAnyOf(blockedStrings)));

        mergeSorted(timelines).limit(MaximumFeedLength).forEach(renderer::render);
    }

    private LazySeq<Message> mergeSorted(LazySeq<LazySeq<Message>> timelines) {
        return timelines.reduce(LazySeq.empty(), this::mergeSorted);
    }

    private LazySeq<Message> mergeSorted(LazySeq<Message> a, LazySeq<Message> b) {
        if (a.isEmpty()) {
            return b;
        }

        if (b.isEmpty()) {
            return a;
        }

        if (MessageComparator.compare(a.head(), b.head()) <= 0) {
            return LazySeq.cons(a.head(), () -> mergeSorted(a.tail(), b));
        } else {
            return LazySeq.cons(b.head(), () -> mergeSorted(a, b.tail()));
        }
    }
}

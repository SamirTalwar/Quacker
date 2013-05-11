package com.noodlesandwich.quacker.message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.ui.TimelineRenderer;
import com.noodlesandwich.quacker.user.Profile;

public class InMemoryFeed implements Feed {
    private final List<Profile> followees = new ArrayList<>();

    @Override
    public void follow(Profile profile) {
        this.followees.add(profile);
    }

    @Override
    public void renderTo(FeedRenderer renderer) {
        SortedSet<Message> feedMessages = new TreeSet<>(new ReverseComparator<>());

        for (Profile profile : followees) {
            profile.renderTimelineTo(new TimelineRenderer() {
                @Override
                public void render(Message message) {
                    feedMessages.add(message);
                }
            });
        }

        for (Message message : feedMessages) {
            renderer.render(message);
        }
    }

    private static class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {
        @Override
        public int compare(T a, T b) {
            return b.compareTo(a);
        }
    }
}

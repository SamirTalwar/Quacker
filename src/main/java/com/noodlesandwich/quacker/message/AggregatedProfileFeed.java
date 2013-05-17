package com.noodlesandwich.quacker.message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.user.Profile;

public class AggregatedProfileFeed implements Feed {
    private final List<Profile> followees = new ArrayList<>();

    public AggregatedProfileFeed(Profile userProfile) {
        followees.add(userProfile);
    }

    @Override
    public void follow(Profile profile) {
        this.followees.add(profile);
    }

    @Override
    public void renderTo(FeedRenderer renderer) {
        List<Message> feedMessages = new ArrayList<>(MAXIMUM_FEED_LENGTH);

        Map<Profile, Message> nextMessages = new HashMap<>(followees.size());
        Map<Profile, Iterator<Message>> timelines = new HashMap<>(followees.size());
        for (Profile profile : followees) {
            timelines.put(profile, profile.iterator());
            nextMessages.put(profile, null);
        }

        while (feedMessages.size() < MAXIMUM_FEED_LENGTH) {
            for (Profile profile : followees) {
                if (nextMessages.get(profile) == null) {
                    Iterator<Message> timeline = timelines.get(profile);
                    if (timeline.hasNext()) {
                         nextMessages.put(profile, timeline.next());
                    }
                }
            }

            NavigableMap<Message, Profile> potentialNextMessages = new TreeMap<>(new ReverseComparator<Message>());
            for (Map.Entry<Profile, Message> nextMessage : nextMessages.entrySet()) {
                Profile profile = nextMessage.getKey();
                Message message = nextMessage.getValue();
                if (message != null) {
                    potentialNextMessages.put(message, profile);
                }
            }
            if (potentialNextMessages.isEmpty()) {
                break;
            }

            Map.Entry<Message, Profile> nextMessage = potentialNextMessages.firstEntry();
            feedMessages.add(nextMessage.getKey());
            nextMessages.put(nextMessage.getValue(), null);
        }

        int count = 0;
        for (Message message : feedMessages) {
            if (count == MAXIMUM_FEED_LENGTH) {
                break;
            }
            renderer.render(message);
            count++;
        }
    }

    private static class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {
        @Override
        public int compare(T a, T b) {
            return b.compareTo(a);
        }
    }
}

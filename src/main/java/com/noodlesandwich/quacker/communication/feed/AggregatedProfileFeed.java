package com.noodlesandwich.quacker.communication.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
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
        List<Message> feedMessages = new ArrayList<>(MaximumFeedLength);

        Map<Profile, Message> nextMessages = new HashMap<>(followees.size());
        Map<Profile, Iterator<Message>> timelines = new HashMap<>(followees.size());
        for (Profile profile : followees) {
            Iterator<Message> timeline = profile.iterator();
            timelines.put(profile, timeline);
            while (timeline.hasNext() && !nextMessages.containsKey(profile)) {
                Message nextMessage = timeline.next();
                if (!nextMessage.isBlockedByAnyOf(blockedStrings)) {
                    nextMessages.put(profile, nextMessage);
                }
            }
        }

        while (feedMessages.size() < MaximumFeedLength) {
            NavigableMap<Message, Profile> potentialNextMessages = new TreeMap<>(MessageComparator);
            for (Map.Entry<Profile, Message> nextMessage : nextMessages.entrySet()) {
                Profile profile = nextMessage.getKey();
                Message message = nextMessage.getValue();
                potentialNextMessages.put(message, profile);
            }
            if (potentialNextMessages.isEmpty()) {
                break;
            }

            Map.Entry<Message, Profile> nextMessage = potentialNextMessages.firstEntry();
            feedMessages.add(nextMessage.getKey());

            Profile profile = nextMessage.getValue();
            Iterator<Message> timeline = timelines.get(profile);
            nextMessages.remove(profile);
            while (timeline.hasNext() && !nextMessages.containsKey(profile)) {
                Message nextTimelineMessage = timeline.next();
                if (!nextTimelineMessage.isBlockedByAnyOf(blockedStrings)) {
                    nextMessages.put(profile, nextTimelineMessage);
                }
            }
        }

        int count = 0;
        for (Message message : feedMessages) {
            if (count == MaximumFeedLength) {
                break;
            }
            renderer.render(message);
            count++;
        }
    }
}

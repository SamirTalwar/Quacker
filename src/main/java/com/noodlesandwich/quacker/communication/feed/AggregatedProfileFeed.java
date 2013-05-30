package com.noodlesandwich.quacker.communication.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.ui.FeedRenderer;
import com.noodlesandwich.quacker.users.Profile;

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
        List<Message> feedMessages = new ArrayList<>(MaximumFeedLength);

        Map<Profile, Message> nextMessages = new HashMap<>(followees.size());
        Map<Profile, Iterator<Message>> timelines = new HashMap<>(followees.size());
        for (Profile profile : followees) {
            Iterator<Message> iterator = profile.iterator();
            timelines.put(profile, iterator);
            if (iterator.hasNext()) {
                nextMessages.put(profile, timelines.get(profile).next());
            }
        }

        while (feedMessages.size() < MaximumFeedLength) {
            NavigableMap<Message, Profile> potentialNextMessages = new TreeMap<>(Collections.reverseOrder());
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
            if (timeline.hasNext()) {
                nextMessages.put(profile, timeline.next());
            } else {
                nextMessages.remove(profile);
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

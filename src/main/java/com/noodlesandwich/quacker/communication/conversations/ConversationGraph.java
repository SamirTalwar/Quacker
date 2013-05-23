package com.noodlesandwich.quacker.communication.conversations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.inject.Singleton;
import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.communication.messages.MessageListener;
import com.noodlesandwich.quacker.communication.messages.NonExistentMessageException;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.User;

@Singleton
public class ConversationGraph implements Conversations, MessageListener {
    public static final int BeforeLimit = 10;
    public static final int AfterLimit = 10;
    public static final Pattern StartsWithMention = Pattern.compile("^@([A-Za-z0-9]+)");

    private final Map<Id, Message> messages = new HashMap<>();
    private final TreeMultimap<String, Message> timelines = TreeMultimap.create();
    private final Multimap<Message, Message> replies = HashMultimap.create();
    private final Map<Message, Message> repliesInverse = new HashMap<>();

    @Override
    public void publish(Id id, User author, String text, Instant timestamp) {
        Message message = new Message(id, author, text, timestamp);
        messages.put(id, message);

        String authorUsername = author.getUsername();
        timelines.put(authorUsername, message);

        Matcher textStartsWithMention = StartsWithMention.matcher(text);
        if (textStartsWithMention.find()) {
            String replyee = textStartsWithMention.group(1);
            if (timelines.containsKey(replyee)) {
                Message originalMessage = timelines.get(replyee).lower(message);
                replies.put(originalMessage, message);
                repliesInverse.put(message, originalMessage);
            }
        }
    }

    @Override
    public Conversation conversationAround(Id messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NonExistentMessageException(messageId);
        }

        Message originalMessage = messages.get(messageId);
        Message rootMessage = rootMessageOf(originalMessage);
        NavigableSet<Message> conversation = repliesTo(rootMessage, originalMessage);

        Collection<Message> snippet = new ArrayList<>(BeforeLimit + 1 + AfterLimit);
        snippet.add(originalMessage);

        Iterator<Message> before = conversation.headSet(originalMessage, false).descendingIterator();
        for (int i = 0; before.hasNext() && i < BeforeLimit; ++i) {
            snippet.add(before.next());
        }

        Iterator<Message> after = conversation.tailSet(originalMessage, false).iterator();
        for (int i = 0; after.hasNext() && i < AfterLimit; ++i) {
            snippet.add(after.next());
        }

        return new SortedConversation(snippet);
    }

    private Message rootMessageOf(Message message) {
        Message rootMessage = message;
        int count = 0;
        while (repliesInverse.containsKey(rootMessage) && count < BeforeLimit) {
            rootMessage = repliesInverse.get(rootMessage);
            count++;
        }
        return rootMessage;
    }

    private NavigableSet<Message> repliesTo(Message message, Message originalMessage) {
        NavigableSet<Message> conversation = new TreeSet<>();
        Queue<Message> nextMessages = new PriorityQueue<>();
        nextMessages.add(message);

        boolean counting = false;
        int count = 0;
        while (!nextMessages.isEmpty() && count < AfterLimit) {
            Message nextMessage = nextMessages.remove();
            conversation.add(nextMessage);
            nextMessages.addAll(replies.get(nextMessage));

            if (counting) {
                count++;
            }
            if (nextMessage == originalMessage) {
                counting = true;
            }
        }
        return conversation;
    }
}

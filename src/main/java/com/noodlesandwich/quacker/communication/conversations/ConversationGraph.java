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
        NavigableSet<Message> before = messagesBefore(originalMessage);
        NavigableSet<Message> after = messagesAfter(originalMessage);

        Collection<Message> snippet = new ArrayList<>(BeforeLimit + 1 + AfterLimit);
        snippet.add(originalMessage);

        Iterator<Message> beforeIterator = before.descendingIterator();
        for (int i = 0; beforeIterator.hasNext() && i < BeforeLimit; ++i) {
            snippet.add(beforeIterator.next());
        }

        Iterator<Message> afterIterator = after.iterator();
        for (int i = 0; afterIterator.hasNext() && i < AfterLimit; ++i) {
            snippet.add(afterIterator.next());
        }

        return new SortedConversation(snippet);
    }

    private NavigableSet<Message> messagesBefore(Message message) {
        NavigableSet<Message> conversation = new TreeSet<>();
        Message nextMessage = repliesInverse.get(message);

        int count = 0;
        while (nextMessage != null && count < BeforeLimit) {
            conversation.add(nextMessage);
            nextMessage = repliesInverse.get(nextMessage);
            count++;
        }
        return conversation;
    }

    private NavigableSet<Message> messagesAfter(Message message) {
        NavigableSet<Message> conversation = new TreeSet<>();
        Queue<Message> nextMessages = new PriorityQueue<>();
        nextMessages.addAll(replies.get(message));

        int count = 0;
        while (!nextMessages.isEmpty() && count <= AfterLimit) {
            Message nextMessage = nextMessages.remove();
            conversation.add(nextMessage);
            nextMessages.addAll(replies.get(nextMessage));
            count++;
        }
        return conversation;
    }
}

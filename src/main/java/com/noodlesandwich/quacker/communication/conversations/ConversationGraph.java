package com.noodlesandwich.quacker.communication.conversations;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
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

        Message rootMessage = rootMessageOf(messageId);
        Collection<Message> conversation = repliesTo(rootMessage);
        return new SortedConversation(conversation);
    }

    private Message rootMessageOf(Id messageId) {
        Message rootMessage = messages.get(messageId);
        while (repliesInverse.containsKey(rootMessage)) {
            rootMessage = repliesInverse.get(rootMessage);
        }
        return rootMessage;
    }

    private Collection<Message> repliesTo(Message message) {
        Collection<Message> conversation = new ArrayList<>();
        Queue<Message> nextMessages = new ArrayDeque<>();
        nextMessages.add(message);

        while (!nextMessages.isEmpty()) {
            Message nextMessage = nextMessages.remove();
            conversation.add(nextMessage);
            nextMessages.addAll(replies.get(nextMessage));
        }
        return conversation;
    }
}

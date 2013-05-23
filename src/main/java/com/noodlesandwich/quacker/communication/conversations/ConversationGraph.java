package com.noodlesandwich.quacker.communication.conversations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private final Map<String, NavigableSet<Message>> timelines = new HashMap<>();
    private final Map<Message, Message> replies = new HashMap<>();

    @Override
    public void publish(Id id, User author, String text, Instant timestamp) {
        Message message = new Message(id, author, text, timestamp);
        messages.put(id, message);

        String authorUsername = author.getUsername();
        if (!timelines.containsKey(authorUsername)) {
            timelines.put(authorUsername, new TreeSet<Message>());
        }
        timelines.get(authorUsername).add(message);

        Matcher textStartsWithMention = StartsWithMention.matcher(text);
        if (textStartsWithMention.find()) {
            String replyee = textStartsWithMention.group(1);
            Message originalMessage = timelines.get(replyee).last();
            replies.put(originalMessage, message);
        }
    }

    @Override
    public Conversation conversationAround(Id messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NonExistentMessageException(messageId);
        }
        List<Message> conversation = new ArrayList<>();
        Message message = messages.get(messageId);
        while (message != null) {
            conversation.add(message);
            message = replies.get(message);
        }
        return new SortedConversation(conversation);
    }
}

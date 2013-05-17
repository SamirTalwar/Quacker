package com.noodlesandwich.quacker.message;

import java.time.Instant;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.ui.MessageRenderer;

public class Message implements Comparable<Message> {
    private final Id id;
    private final String text;
    private final Instant timestamp;

    public Message(Id id, String text, Instant timestamp) {
        if (text.isEmpty()) {
            throw new EmptyMessageException(timestamp);
        }
        if (text.length() > 140) {
            throw new MessageTooLongException(text, timestamp);
        }

        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }

    public void renderTo(MessageRenderer renderer) {
        renderer.render(id.getValue(), text, timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Message)) {
            return false;
        }

        Message other = (Message) o;
        return id.equals(other.id)
            && text.equals(other.text)
            && timestamp.equals(other.timestamp);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }

    @Override
    public int compareTo(Message other) {
        int result;

        result = timestamp.compareTo(other.timestamp);
        if (result != 0) {
            return result;
        }

        result = text.compareTo(other.text);
        return result;
    }

    @Override
    public String toString() {
        return text;
    }
}

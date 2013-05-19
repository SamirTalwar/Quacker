package com.noodlesandwich.quacker.communication.conversations;

import com.noodlesandwich.quacker.id.Id;

public interface Conversations {
    Conversation conversationAround(Id messageId);
}

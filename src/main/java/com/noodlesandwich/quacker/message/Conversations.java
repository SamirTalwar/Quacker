package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.id.Id;

public interface Conversations {
    Conversation conversationAround(Id messageId);
}

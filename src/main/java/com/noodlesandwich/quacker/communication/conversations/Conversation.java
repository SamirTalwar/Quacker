package com.noodlesandwich.quacker.communication.conversations;

import com.noodlesandwich.quacker.ui.ConversationRenderer;

public interface Conversation {
    void renderConversationTo(ConversationRenderer renderer);
}

package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.ConversationRenderer;

public interface Conversation {
    void renderConversationTo(ConversationRenderer renderer);
}

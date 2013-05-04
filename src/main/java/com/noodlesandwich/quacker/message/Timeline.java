package com.noodlesandwich.quacker.message;

import com.noodlesandwich.quacker.ui.MessageRenderer;

public interface Timeline {
    void renderTo(MessageRenderer renderer);
}

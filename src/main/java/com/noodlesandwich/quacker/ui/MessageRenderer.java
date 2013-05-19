package com.noodlesandwich.quacker.ui;

import java.time.Instant;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.user.User;

public interface MessageRenderer {
    void render(Id id, User author, String text, Instant timestamp);
}

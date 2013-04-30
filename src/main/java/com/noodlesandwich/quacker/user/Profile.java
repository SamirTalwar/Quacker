package com.noodlesandwich.quacker.user;

import com.noodlesandwich.quacker.ui.TimelineRenderer;

public interface Profile {
    void renderTimelineTo(TimelineRenderer renderer);
}

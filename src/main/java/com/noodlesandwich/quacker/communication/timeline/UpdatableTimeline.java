package com.noodlesandwich.quacker.communication.timeline;

import com.noodlesandwich.quacker.communication.messages.Message;

public interface UpdatableTimeline extends Timeline {
    void publish(Message message);
}

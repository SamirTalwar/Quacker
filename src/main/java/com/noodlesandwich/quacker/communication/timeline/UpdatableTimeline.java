package com.noodlesandwich.quacker.communication.timeline;

import com.noodlesandwich.quacker.communication.messages.Message;
import com.noodlesandwich.quacker.id.Id;

public interface UpdatableTimeline extends Timeline {
    void publish(Id messageId, Message message);
}

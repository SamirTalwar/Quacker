package com.noodlesandwich.quacker.message;

public interface UpdatableTimeline extends Timeline {
    void publish(Message message);
}

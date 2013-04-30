package com.noodlesandwich.quacker.client;

public interface Client {
    void publish(String message);
    void openTimelineOf(String username);
}

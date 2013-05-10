package com.noodlesandwich.quacker.client;

public interface Client {
    void publish(String message);
    void follow(String followee);

    void openTimelineOf(String username);
    void openFeed();
}

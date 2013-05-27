package com.noodlesandwich.quacker.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.noodlesandwich.quacker.communication.conversations.Conversation;
import com.noodlesandwich.quacker.id.Id;
import com.noodlesandwich.quacker.users.Profile;
import com.noodlesandwich.quacker.users.User;

public interface RemoteServer extends Remote {
    void registerUserNamed(String username) throws RemoteException;
    User authenticatedUserNamed(String username) throws RemoteException;
    Profile profileFor(String username) throws RemoteException;
    Conversation conversationAround(Id messageId) throws RemoteException;
}

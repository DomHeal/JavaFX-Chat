package com.client;

/**
 * Created by Dominic on 24-Oct-15.
 */
import java.io.*;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server.
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2;
    private String username;
    private String message;

    // constructor
    ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    // getters
    String getUsername() {
        return username;
    }
    String getMessage() {
        return message;
    }
}


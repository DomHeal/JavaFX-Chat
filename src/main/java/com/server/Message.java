package com.server;

import java.io.Serializable;

/**
 * Created by Dominic on 06-Mar-16.
 */
public class Message implements Serializable {
    private final String name;
    private final String type;

    public String getName() {
        return name;
    }

    public String getMsg() {

        return msg;
    }

    private final String msg;

    public Message(String name, String msg, String type){
        this.name = name;
        this.msg = msg;
        this.type = type;
    }
}

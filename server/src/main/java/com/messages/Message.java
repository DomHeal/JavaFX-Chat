package com.messages;

import java.io.Serializable;
import java.util.HashSet;

public class Message implements Serializable {

    private String name;
    private String type;
    private String msg;
    private HashSet userlist;

    public Message() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashSet getUserlist() {
        return userlist;
    }

    public void setUserlist(HashSet userlist) {
        this.userlist = userlist;
    }


}

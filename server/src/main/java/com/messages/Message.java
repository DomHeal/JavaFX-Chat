package com.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Message implements Serializable {

    private String name;
    private String type;
    private String msg;
    private int count;
    private ArrayList<String> list;

    public String getPicture() {
        return picture;
    }

    private String picture;

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

    public ArrayList<String> getUserlist() {
        return list;
    }

    public void setUserlist(HashSet userlist2) {
        this.list = new ArrayList<String>(userlist2);
    }

    public void setOnlineCount(int count){
        this.count = count;
    }

    public int getOnlineCount(){
        return this.count;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}

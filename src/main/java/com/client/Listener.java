package com.client;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

class Listener implements Runnable, Serializable{

    private Socket socket;
    public String hostname;
    public int port;
    public String username;
    BufferedReader in;
    static PrintWriter out;
    public TextArea chat;
    public Controller controller;

    public Listener(String hostname, int port, String username, Controller controller) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.controller = controller;
    }

    public void run() {
        try {
            socket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sockets in and out ready!");

        out.println(username);
        while (true) {
            String line = null;
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null) {
                if (line.startsWith("UserCount:")) {
                    controller.setOnlineLabel(line.substring(10));
                }
                else if (line.startsWith("ClearList:")) {
                    controller.clearUserList();
                }
                else if (line.startsWith("UserListAdd:")){
                    controller.setUserList(line.substring(13));
                }
                else {
                    controller.addToChat(line);
                }
            }
        }
    }

    public static void send(String msg) {
        out.println(msg);
    }
}

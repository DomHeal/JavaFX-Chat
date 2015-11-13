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
    public ChatController controller;
    private boolean isConnected = false;

    public Listener(String hostname, int port, String username, ChatController controller) {
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

        isConnected = true;
        System.out.println("Sockets in and out ready!");

        out.println(username);
        while (true) {
            String message = null;
            try {
                message = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message != null) {
                if (message.startsWith("UserCount:")) {
                    controller.setOnlineLabel(message.substring(10));
                }
                else if (message.startsWith("ClearList:")) {
                    controller.clearUserList();
                }
                else if (message.startsWith("UserListAdd:")){
                    controller.setUserList(message.substring(13));
                }
                else if (message.startsWith("Server:")) {
                    controller.addAsServer(message);
                }
                else {
                    controller.addToChat(message);
                }
            }
        }
    }

    public static void send(String msg) {
        out.println(msg);
    }

}

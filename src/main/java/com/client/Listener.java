package com.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Dominic on 30-Oct-15.
 */
class Listener implements Runnable{

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
        System.out.println("Before" + controller.toString());
        //controller1 = controller;
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
                    System.out.println(line);
                    controller.addToChat(line);
                    System.out.println("Listening Instance:");
                    System.out.println(controller.toString());


            }
        }
    }

    public static void send(String msg) {
        out.println(msg);
    }
}

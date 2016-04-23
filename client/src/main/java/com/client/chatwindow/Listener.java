package com.client.chatwindow;

import com.messages.Message;

import java.io.*;
import java.net.Socket;

public class Listener implements Runnable{

    private final String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public static String username;
    public ChatController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    public Listener(String hostname, int port, String username, String picture, ChatController controller) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.picture = picture;
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
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sockets in and out ready!");

        while (true) {
            Message message = null;
            try {
                message = (Message)input.readObject();
            } catch (IOException | ClassNotFoundException e) {
            }
            if (message != null) {
                System.out.println("COUNT" + message.getOnlineCount());
                switch (message.getType()){
                    case "USER": controller.addToChat(message); break;
                    case "NOTIFICATION": controller.newUserNotification(message); break;
                    case "SERVER": controller.addAsServer(message); break;
                    case "CONNECTED" : controller.setUserList(message); break;
                    case "DISCONNECTD" : controller.setUserList(message); break;
                }
            }
        }
    }

    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType("USER");
        createMessage.setMsg(msg);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType("CONNECTED");
        createMessage.setMsg("has connected!");
        oos.writeObject(createMessage);
    }

    public static void disconnect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType("DISCONNECTD");
        createMessage.setMsg("has disconnect!");
        oos.writeObject(createMessage);
    }

}

package com.client.chatwindow;

import com.client.login.LoginController;
import com.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Listener implements Runnable{

    private static String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public static String username;
    public ChatController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    Logger logger = LoggerFactory.getLogger(Listener.class);

    public Listener(String hostname, int port, String username, String picture, ChatController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.controller = controller;
    }

    public void run() {

        try {
            socket = new Socket(hostname, port);
            LoginController.getInstance().showScene();
        } catch (IOException e) {
            LoginController.getInstance().showErrorDialog("Could not connect to server");
            logger.error("Could not Connect");
        }
        logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());


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
        logger.info("Sockets in and out ready!");

        while (true) {
            Message message = null;
            try {
                message = (Message)input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                controller.logoutScene();
                break;
            }
            if (message != null) {
                logger.debug("Message recieved:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                switch (message.getType()){
                    case "USER": controller.addToChat(message); break;
                    case "NOTIFICATION": controller.newUserNotification(message); break;
                    case "SERVER": controller.addAsServer(message); break;
                    case "CONNECTED" : controller.setUserList(message); break;
                    case "DISCONNECTED" : controller.setUserList(message); break;
                }
            }
        }
    }

    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */
    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType("USER");
        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used to send a connecting message */
    public static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType("CONNECTED");
        createMessage.setMsg("has connected!");
        createMessage.setPicture(picture);
        System.out.println("connect" + picture);
        oos.writeObject(createMessage);
    }
    /* This method is used to send a disconnecting message */
    public static void disconnect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType("DISCONNECTD");
        createMessage.setMsg("has disconnect!");
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
    }

}

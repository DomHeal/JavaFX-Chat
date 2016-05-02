package com.server;

import com.messages.Message;
import com.messages.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class Server {



    private static final int PORT = 9001;
    private static final HashMap<String, User> names = new HashMap<String, User>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();
    private static ArrayList<User> users = new ArrayList<User>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");

        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }

    }


    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private InputStream is;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private static final Logger logger = Logger.getLogger(Handler.class.getClass().getCanonicalName());

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        public void run() {
            try {
                logger.info("Attempting to connect a user...");
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);

                while (true) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        logger.info(inputmsg.getName() + " has " + names.size());
                        System.out.println(currentThread().getName() + " name size : " + names.size());
                        switch (inputmsg.getType()) {
                            case "USER":
                                write(inputmsg);
                                break;
                            case "CONNECTED":
                                addToList(inputmsg);
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                closeConnections();
            }
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) {
                logger.info(firstMessage.getName() + " is trying to connect");
                if (!names.containsKey(firstMessage.getName())) {
                    this.name = firstMessage.getName();
                    writers.add(output);

                    User user = new User();
                    user.setName(firstMessage.getName());
                    user.setPicture(firstMessage.getPicture());

                    names.put(name, user);

                    users.add(user);

                    logger.info(firstMessage.getName() + " has been added to the list");
                    try {
                        addToList(firstMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(name + " added");
                } else {
                    closeConnections();
                    logger.info(firstMessage.getName() + " is already connected");
                }
        }


        private synchronized void removeFromList(String name) throws IOException {
            Message msg = new Message();
            msg.setMsg("Welcome, You have now joined the server! Enjoy chatting!");
            msg.setType("DISCONNECTED");
            msg.setName("SERVER");
            msg.setUserlist(names);
            write(msg);
        }

        private void addToList(Message msg) throws IOException {
            msg = new Message();
            msg.setMsg("Welcome, You have now joined the server! Enjoy chatting!");
            msg.setType("CONNECTED");
            msg.setName("SERVER");
            write(msg);
        }

        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                msg.setUserlist(names);
                msg.setUsers(users);
                msg.setOnlineCount(names.size());
                System.out.println(names.size());
                logger.info(writer.toString() + " " + msg.getName() + " " + msg.getUserlist().toString());
                try {
                    writer.writeObject(msg);
                    writer.reset();
                } catch (Exception ex){
                    closeConnections();
                }
            }
        }

        private void closeConnections() {
            if (name != null) {
                names.remove(name);
                System.out.println("User: " + name + " has been removed!");
            }
            if (output != null) {
                writers.remove(output);
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                removeFromList(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

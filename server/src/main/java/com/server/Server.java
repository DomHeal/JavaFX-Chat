package com.server;

import com.exception.DuplicateUsernameException;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {

    /* Setting up variables */
    private static final int PORT = 9001;
    private static final HashMap<String, User> names = new HashMap<>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static ArrayList<User> users = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        logger.info("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);

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
        private Logger logger = LoggerFactory.getLogger(Handler.class);
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        public void run() {
            logger.info("Attempting to connect a user...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);
                writers.add(output);
                sendNotification(firstMessage);
                addToList();

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        logger.info(inputmsg.getType() + " - " + inputmsg.getName() + ": " + inputmsg.getMsg());
                        switch (inputmsg.getType()) {
                            case USER:
                                write(inputmsg);
                                break;
                            case VOICE:
                                write(inputmsg);
                                break;
                            case CONNECTED:
                                addToList();
                                break;
                            case STATUS:
                                changeStatus(inputmsg);
                                break;
                        }
                    }
                }
            } catch (SocketException socketException) {
                logger.error("Socket Exception for user " + name);
            } catch (DuplicateUsernameException duplicateException){
                logger.error("Duplicate Username : " + name);
            } catch (Exception e){
                logger.error("Exception in run() method for user: " + name, e);
            } finally {
                closeConnections();
            }
        }

        private Message changeStatus(Message inputmsg) throws IOException {
            logger.debug(inputmsg.getName() + " has changed status to  " + inputmsg.getStatus());
            Message msg = new Message();
            msg.setName(user.getName());
            msg.setType(MessageType.STATUS);
            msg.setMsg("");
            User userObj = names.get(name);
            userObj.setStatus(inputmsg.getStatus());
            write(msg);
            return msg;
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) throws DuplicateUsernameException {
            logger.info(firstMessage.getName() + " is trying to connect");
            if (!names.containsKey(firstMessage.getName())) {
                this.name = firstMessage.getName();
                user = new User();
                user.setName(firstMessage.getName());
                user.setStatus(Status.ONLINE);
                user.setPicture(firstMessage.getPicture());

                users.add(user);
                names.put(name, user);

                logger.info(name + " has been added to the list");
            } else {
                logger.error(firstMessage.getName() + " is already connected");
                throw new DuplicateUsernameException(firstMessage.getName() + " is already connected");
            }
        }

        private Message sendNotification(Message firstMessage) throws IOException {
            Message msg = new Message();
            msg.setMsg("has joined the chat.");
            msg.setType(MessageType.NOTIFICATION);
            msg.setName(firstMessage.getName());
            msg.setPicture(firstMessage.getPicture());
            write(msg);
            return msg;
        }


        private Message removeFromList() throws IOException {
            logger.debug("removeFromList() method Enter");
            Message msg = new Message();
            msg.setMsg("has left the chat.");
            msg.setType(MessageType.DISCONNECTED);
            msg.setName("SERVER");
            msg.setUserlist(names);
            write(msg);
            logger.debug("removeFromList() method Exit");
            return msg;
        }

        /*
         * For displaying that a user has joined the server
         */
        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.setMsg("Welcome, You have now joined the server! Enjoy chatting!");
            msg.setType(MessageType.CONNECTED);
            msg.setName("SERVER");
            write(msg);
            return msg;
        }

        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                msg.setUserlist(names);
                msg.setUsers(users);
                msg.setOnlineCount(names.size());
                writer.writeObject(msg);
                writer.reset();
            }
        }

        /*
         * Once a user has been disconnected, we close the open connections and remove the writers
         */
        private synchronized void closeConnections()  {
            logger.debug("closeConnections() method Enter");
            logger.info("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (name != null) {
                names.remove(name);
                logger.info("User: " + name + " has been removed!");
            }
            if (user != null){
                users.remove(user);
                logger.info("User object: " + user + " has been removed!");
            }
            if (output != null){
                writers.remove(output);
                logger.info("Writer object: " + user + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            logger.debug("closeConnections() method Exit");
        }
    }
}

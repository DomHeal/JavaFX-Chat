package com.server;

import com.messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {

    private static final int PORT = 9001;
    private static final HashSet<String> names = new HashSet<String>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
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
        private InputStream is;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);


                Message nameCheck = (Message) input.readObject();
                synchronized (names) {
                    if (!names.contains(nameCheck.getName())) {
                        writers.add(output);
                        this.name = nameCheck.getName();
                        names.add(name);

                        addToList(nameCheck);
                        System.out.println(name + " added");
                    } else {
                        System.out.println("duplicate name");
                    }

                }

                while (true) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
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
            } finally {
                if (name != null) {
                    names.remove(name);
                    System.out.println("User: " + name + " has been removed!");
                    try {
                        removeFromList(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    writers.remove(output);
                }
            }
            try {
                output.close();
            } catch (IOException e) {
            }
        }


        private synchronized void removeFromList(String name) throws IOException {
            names.remove(name);
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
                System.out.println(msg.getUserlist().size());
                writer.writeObject(msg);
            }
        }
    }
}

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
                new Handler(listener.accept()).start();

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

                writers.add(output);

                while (true) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        System.out.println(inputmsg.getMsg());
                        switch (inputmsg.getType()) {
                            case "USER":
                                write(inputmsg);break;
                            case "CONNECTED":
                                addToList(inputmsg);
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (output != null) {
                    writers.remove(output);
                }
                try {
                    output.close();
                } catch (IOException e) {}
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

        private synchronized void addToList(Message msg) throws IOException {
            if (!names.contains(msg.getName())) {
                names.add(msg.getName());
                msg = new Message();
                msg.setMsg("Welcome, You have now joined the server! Enjoy chatting!");
                msg.setType("CONNECTED");
                msg.setName("SERVER");
                msg.setUserlist(names);
                System.out.println(names.size());
                write(msg);
            }
        }

        public void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                writer.writeObject(msg);
            }
        }
    }
}
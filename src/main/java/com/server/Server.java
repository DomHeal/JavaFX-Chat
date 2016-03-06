package com.server;

import com.client.messages.Message;

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

                synchronized (names) {
                    if (!names.contains(name)) {
                        System.out.println("Server: Welcome " + name + ", You have now joined the server! Enjoy chatting!");
                        names.add(name);
                    }
                }


                writers.add(output);


                while (true) {
                    Message inputmsg = ((Message) input.readObject());
                    if (inputmsg != null) {
                        for (ObjectOutputStream writer : writers) {
                            writer.writeObject(inputmsg);
                            System.out.println(inputmsg.getName() + ": " + inputmsg.getMsg());
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
                }
                if (output != null) {
                    writers.remove(output);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
package com.server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {

    private static final int PORT = 9001;
    private static final HashSet<String> names = new HashSet<String>();
    private static final ArrayList<String> images = new ArrayList<>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

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
        private BufferedReader in;
        private PrintWriter out;
        private static String userList;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            out.println("Server: Welcome " + name + ", You have now joined the server! Enjoy chatting!");
                            names.add(name);
                            //images.add()
                            break;
                        }
                    }
                }
                writers.add(out);

                createUserList();

                sendClearList();

                sendUsersInformation();

                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println(name + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    names.remove(name);
                    System.out.println("User: " + name + " has been removed!");
                }
                if (out != null) {
                    writers.remove(out);
                }
                for (PrintWriter writer : writers) {
                    writer.println("UserCount:" + names.size());
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void createUserList() {
            userList = "UserListAdd: ";
            for (String singlename : names) {
                userList += singlename + ",";
            }
        }

        private void sendUsersInformation() {
            for (PrintWriter writer : writers) {
                writer.println("UserCount:" + names.size());
                writer.println(userList);
            }
        }

        private void sendClearList() {
            for (PrintWriter writer : writers) {
                writer.println("ClearList:");
            }
        }
    }
}
package com.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextField hostnameTextfield;
    @FXML private TextField portTextfield;
    @FXML private TextField usernameTextfield;
    private ServerSocket server;
    private Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    @FXML private TextArea messageBox;
    @FXML private TextArea chatFlow;

    public void loginButtonAction() throws IOException {

        String hostname = hostnameTextfield.getText().toString();
        int port = Integer.parseInt(portTextfield.getText().toString());
        String username = usernameTextfield.getText().toString();

        Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
        new MainInterface(stage);
        new Listener(socket, hostname, port, username).start();
    }

    public void sendButtonAction(){
        String msg = messageBox.getText().toString();
        chatFlow.appendText("hello" + "\n");
        Listener.send(msg);
        messageBox.setText("");
    }

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/socketchat.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }
}

class Listener extends Thread {

    private Socket socket;
    public String hostname;
        public int port;
        public String username;
        BufferedReader in;
        static PrintWriter out;


        public Listener(Socket socket, String hostname, int port, String username) {
            this.socket = socket;
            this.hostname = hostname;
        this.port = port;
        this.username = username;
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

        while (true) {
            String line = null;
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null) {
                    System.out.println(line);
                    //messageBox.append(line + "\n");
                }
            }
        }

    public static void send(String msg) {
        out.print(msg);
    }
}





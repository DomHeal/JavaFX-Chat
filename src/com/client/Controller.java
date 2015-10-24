package com.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextField hostnameTextfield;
    @FXML private TextField portTextfield;
    @FXML private TextField usernameTextfield;
    private ServerSocket server;
    private Socket socket;


    public void loginButtonAction(){

        String hostname = hostnameTextfield.getText().toString();
        int port = Integer.parseInt(portTextfield.getText().toString());
        String username = usernameTextfield.getText().toString();

        try {
            socket = new Socket(hostname, port);
            new MainInterface();

        } catch (IOException e) {
            e.printStackTrace();
        }

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



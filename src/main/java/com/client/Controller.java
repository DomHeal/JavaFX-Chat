package com.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private TextField hostnameTextfield;
    @FXML
    private TextField portTextfield;
    @FXML
    private TextField usernameTextfield;

    private static String username;
    public BufferedReader in;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Controller.username = username;
    }

    public void loginButtonAction() throws IOException {

        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        username = usernameTextfield.getText();

        Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
        stage.setResizable(true);
        stage.setMinWidth(1020);
        stage.setHeight(600);
        new MainInterface(stage, hostname, port, username);
    }

    
}






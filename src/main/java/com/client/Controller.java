package com.client;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.*;


public class Controller {
    private static Controller instance;
    public static Controller getInstance() {
        if(instance == null)
            instance = new Controller();
        return instance;
    }
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

    public void loginButtonAction() throws IOException {
        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        username = usernameTextfield.getText();

        Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
        stage.setResizable(true);
        stage.setMinWidth(1020);
        stage.setHeight(600);

        FXMLLoader y = new FXMLLoader(getClass().getResource("/styles/maindesign.fxml"));
        Parent window3 = (Pane) y.load();

        Scene  newScene = new Scene(window3);

        stage.setScene(newScene);

        Controller con = y.<Controller>getController();
        System.out.println("Controller collection: " + con.toString());

        Thread x = new Thread(new Listener(hostname, port, username, con));
        x.start();
    }

    @FXML
    private TextArea messageBox;
    @FXML
    public TextArea chatFlow;

    public void sendButtonAction() {
        String msg = messageBox.getText();
        messageBox.setText("");
        Listener.send(msg);
    }

    public void addToChat(String msg) {
        try {
            chatFlow.appendText(msg + "\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}






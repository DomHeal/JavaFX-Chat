package com.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.*;

public class Controller{

    @FXML
    private TextField hostnameTextfield;
    @FXML
    private TextField portTextfield;
    @FXML
    private TextField usernameTextfield;

    public BufferedReader in;

    @FXML
    private TextArea messageBox;
    @FXML
    public TextArea chatFlow;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label onlineCountLabel;
    @FXML
    private ListView userList;

    ObservableList<String> items =FXCollections.observableArrayList ();

    public void loginButtonAction() throws IOException {
        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        String username = usernameTextfield.getText();

        Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
        stage.setResizable(true);
        stage.setMinWidth(1020);
        stage.setHeight(620);

        FXMLLoader y = new FXMLLoader(getClass().getResource("/styles/maindesign.fxml"));
        Parent window3 = (Pane) y.load();

        Scene newScene = new Scene(window3);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.setScene(newScene);

        Controller con = y.<Controller>getController();
        con.setUsernameLabel(username);

        Thread x = new Thread(new Listener(hostname, port, username, con));
        x.start();
    }


    public void sendButtonAction() {
        String msg = messageBox.getText();
        if(!messageBox.getText().isEmpty()) {
            Listener.send(msg);
            messageBox.setText("");
        }
    }

    public void addToChat(String msg) {
        try {
            chatFlow.appendText(msg + "\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUserList(String userListnames) {

        Platform.runLater(() -> {
            items.add(userListnames);
            userList.setItems(items);
        });
    }
}
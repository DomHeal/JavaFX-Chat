package com.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

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
    @FXML
    private TextField imgURLTextfield;
    @FXML
    private ImageView userImageView;

    ObservableList<String> items = FXCollections.observableArrayList ();

    public void loginButtonAction() throws IOException {
        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        String username = usernameTextfield.getText();

        FXMLLoader y = new FXMLLoader(getClass().getResource("/styles/maindesign.fxml"));
        Parent window3 = (Pane) y.load();
        Controller con = y.<Controller>getController();

        Thread x = new Thread(new Listener(hostname, port, username, con));
        x.start();

        Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
        stage.setResizable(true);
        stage.setMinWidth(1020);
        stage.setHeight(620);

        Scene newScene = new Scene(window3);
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(newScene);

        con.setUsernameLabel(username);
        con.setImageLabel(imgURLTextfield.getText());


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

    public void setImageLabel(String imageURL){
        this.userImageView.setImage(new Image(imageURL,50,50,false,false));
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUserList(String userListnames) {
        Platform.runLater(() -> {
            String[] userlist = userListnames.split(",");
            for (String name : userlist) {
                items.add(name);
            }
            userList.setItems(items);
            userList.setCellFactory(list -> new CellRenderer()
            );
            newUserNotification(userlist);
        });
    }

    private void newUserNotification(String[] userlist) {
        //Image profileImg = new Image(,50,50,false,false);
        TrayNotification tray = new TrayNotification();
        tray.setTitle("A new user has joined!");
        tray.setMessage(userlist[userlist.length-1] + " has joined the JavaFX Chatroom!");
        tray.setRectangleFill(Paint.valueOf("#2A9A84"));
        tray.setAnimationType(AnimationType.POPUP);
        //tray.setImage(profileImg);
        tray.showAndDismiss(Duration.seconds(5));
    }

    public void clearUserList() {
        Platform.runLater(() -> {
            userList.getItems().clear();
            items.removeAll();
            userList.getItems().removeAll();
            System.out.println("cleared lists");
        });
        }

    public void sendMethod(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
            messageBox.setText("");
        }
    }

    public void closeApplication(){
        System.exit(1);
    }
}


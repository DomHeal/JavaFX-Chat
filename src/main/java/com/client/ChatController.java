package com.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import rumorsapp.BubbleSpec;
import rumorsapp.BubbledLabel;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class ChatController implements Initializable{

    public BufferedReader in;

    @FXML private TextArea messageBox;
    @FXML private Label usernameLabel;
    @FXML private Label onlineCountLabel;
    @FXML private ListView userList;
    @FXML private ImageView userImageView;
    @FXML VBox chatPane;
    @FXML ListView statusList;

    ObservableList<String> items = FXCollections.observableArrayList ();

    public void sendButtonAction() {
        String msg = messageBox.getText();
        if(!messageBox.getText().isEmpty()) {
            Listener.send(msg);
            messageBox.setText("");
        }
    }

    public synchronized void addToChat(String msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/profile_circle.png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();

                bl6.setText(msg);
                bl6.setBackground(new Background(new BackgroundFill(Color.WHITE,
                        null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);

                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            chatPane.getChildren().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/profile_circle.png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();

                bl6.setText(msg);
                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                        null, null)));
                HBox x = new HBox();
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);

                return x;
            }
        };

        yourMessages.setOnSucceeded(event -> {
            chatPane.getChildren().add(yourMessages.getValue());
        });

        System.out.println(msg);
        System.out.println();
        if (msg.startsWith(usernameLabel.getText())){
            msg.substring(usernameLabel.getText().length()+1);
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }


    }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }

    public void setImageLabel() throws IOException {
        this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/profile_circle.png").toString()));
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUserList(String userListnames) {
        Platform.runLater(() -> {
            String[] userlist = userListnames.split(",");
            Collections.addAll(items, userlist);
            userList.setItems(items);
            userList.setCellFactory(list -> new CellRenderer());
            statusList.setItems(items);
            statusList.setCellFactory(list -> new StatusCellRenderer());
            statusList.setMouseTransparent( true );
            statusList.setFocusTraversable( false );

            newUserNotification(userlist);
        });
    }

    private void newUserNotification(String[] userlist) {
        Image profileImg = new Image(getClass().getClassLoader().getResource("images/profile_circle.png").toString(),50,50,false,false);
        TrayNotification tray = new TrayNotification();
        tray.setTitle("A new user has joined!");
        tray.setMessage(userlist[userlist.length-1] + " has joined the JavaFX Chatroom!");
        tray.setRectangleFill(Paint.valueOf("#2C3E50"));
        tray.setAnimationType(AnimationType.POPUP);
        tray.setImage(profileImg);
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


    public synchronized void addAsServer(String msg) {
        Task<HBox> task = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                BubbledLabel bl6 = new BubbledLabel();
                bl6.setText(msg);
                bl6.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,
                        null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_BOTTOM);
                x.setAlignment(Pos.CENTER);
                x.getChildren().addAll(bl6);
                return x;
            }
        };
        task.setOnSucceeded(event -> {
            chatPane.getChildren().add(task.getValue());
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setImageLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImageLabel(String selectedPicture) {
        switch (selectedPicture) {
            case "Dominic": this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/profile_circle.png").toString()));
                break;
            case "Sarah": this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/profilegirl.png").toString()));
                break;
            case "Default": this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/blank.png").toString()));
                break;
        }
    }
}
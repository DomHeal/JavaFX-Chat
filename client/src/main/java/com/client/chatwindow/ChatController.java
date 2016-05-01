package com.client.chatwindow;

import com.client.login.MainLauncher;
import com.messages.BubbleSpec;
import com.messages.BubbledLabel;
import com.messages.Message;
import com.messages.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable{

    @FXML private TextArea messageBox;
    @FXML private Label usernameLabel;
    @FXML private Label onlineCountLabel;
    @FXML private ListView userList;
    @FXML private ImageView userImageView;
    @FXML VBox chatPane;
    @FXML ListView statusList;
    @FXML BorderPane borderPane;

    ObservableList<String> items = FXCollections.observableArrayList ();
    private double xOffset;
    private double yOffset;

    public void sendButtonAction() throws IOException {
        String msg = messageBox.getText();
        if(!messageBox.getText().isEmpty()) {
            Listener.send(msg);
            messageBox.setText("");
        }
    }

    public synchronized void addToChat(Message msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture() + ".png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();

                bl6.setText(msg.getName() + ": " + msg.getMsg());
                bl6.setBackground(new Background(new BackgroundFill(Color.WHITE,
                        null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);
                System.out.println("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            chatPane.getChildren().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = userImageView.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();

                bl6.setText(msg.getMsg());
                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                        null, null)));
                HBox x = new HBox();
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };

        yourMessages.setOnSucceeded(event -> chatPane.getChildren().add(yourMessages.getValue()));

        if (msg.getName().equals(usernameLabel.getText())){
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
        this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/Dominic.png").toString()));
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUserList(Message msg) {
        System.out.println("clear list");
        clearUserList();
        Platform.runLater(() -> {
            System.out.println(msg.getUsers().size());
            System.out.println(msg.getUserlist().size());
            ObservableList<User> users = FXCollections.observableList(msg.getUsers());
            for (User user : users){
                System.out.println(user.getName());
            }
            userList.setItems(users);
            userList.setCellFactory(new Callback<ListView<User>, ListCell<User>>(){

                @Override
                public ListCell<User> call(ListView<User> p) {

                    ListCell<User> cell = new ListCell<User>(){

                        @Override
                        protected void updateItem(User user, boolean bln) {
                            super.updateItem(user, bln);
                            setGraphic(null);
                            setText(null);
                            if (user != null) {
                                setText(user.getName());
                                ImageView imageView = new ImageView();
                                Image image = new Image(getClass().getClassLoader().getResource("images/" + user.getPicture() + ".png").toString(),50,50,false,false);
                                setText(user.getName());
                                imageView.setImage(image);
                                setGraphic(imageView);

                            }
                        }

                    };

                    return cell;
                }
            });
            statusList.setItems(users);
            statusList.setCellFactory(new Callback<ListView<User>, ListCell<User>>(){

                @Override
                public ListCell<User> call(ListView<User> p) {

                    ListCell<User> cell = new ListCell<User>(){

                        @Override
                        protected void updateItem(User user, boolean bln) {
                            super.updateItem(user, bln);
                            if (bln) {
                                setGraphic(null);
                                setText(null);
                            }
                            if (user != null) {
                                ImageView imageView = new ImageView();
                                Image image = new Image(getClass().getClassLoader().getResource("images/" + "online" + ".png").toString(),16,16,false,false);
                                imageView.setImage(image);
                                setGraphic(imageView);
                            }
                        }

                    };

                    return cell;
                }
            });
            statusList.setMouseTransparent( true );
            statusList.setFocusTraversable( false );
            setOnlineLabel(String.valueOf(msg.getUserlist().size()));

        });
    }

    public void newUserNotification(Message msg) {
        Platform.runLater(() -> {
            Image profileImg = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture() +".png").toString(),50,50,false,false);
            TrayNotification tray = new TrayNotification();
            tray.setTitle("A new user has joined!");
            tray.setMessage(msg.getName() + " has joined the JavaFX Chatroom!");
            tray.setRectangleFill(Paint.valueOf("#2C3E50"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.setImage(profileImg);
            tray.showAndDismiss(Duration.seconds(5));
        });
    }

    public void clearUserList() {
        Platform.runLater(() -> {
            userList.getItems().clear();
            userList.getItems().removeAll();
            userList.setCellFactory(null);
        });
        }

    public void sendMethod(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
            messageBox.setText("");
        }
    }

    public void closeApplication(){
        System.out.println("close");
        Platform.exit();
    }


    public synchronized void addAsServer(Message msg) {
        Task<HBox> task = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                BubbledLabel bl6 = new BubbledLabel();
                bl6.setText(msg.getMsg());
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
                /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
            yOffset = MainLauncher.getPrimaryStage().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });
    }

    public void setImageLabel(String selectedPicture) {
        switch (selectedPicture) {
            case "Dominic": this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/Dominic.png").toString()));
                break;
            case "Sarah": this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/sarah.png").toString()));
                break;
            case "Default": this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
                break;
        }
    }
}
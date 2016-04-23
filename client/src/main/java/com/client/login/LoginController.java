package com.client.login;

import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Dominic on 12-Nov-15.
 */
public class LoginController implements Initializable {
    @FXML private ImageView Defaultview;
    @FXML private ImageView Sarahview;
    @FXML private ImageView Dominicview;
    @FXML private TextField hostnameTextfield;
    @FXML private TextField portTextfield;
    @FXML private TextField usernameTextfield;
    @FXML private ChoiceBox imagePicker;
    @FXML private Label selectedPicture;
    public static ChatController con;
    @FXML private BorderPane borderPane;

    public void loginButtonAction() throws IOException {
        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        String username = usernameTextfield.getText();
        String picture = selectedPicture.getText();

        FXMLLoader y = new FXMLLoader(getClass().getResource("/styles/maindesign.fxml"));
        Parent window3 = (Pane) y.load();
        con = y.<ChatController>getController();
        Listener listener = new Listener(hostname, port, username, picture, con);
        Thread x = new Thread(listener);
        x.start();

        Stage stage = (Stage) hostnameTextfield.getScene().getWindow();
        stage.setResizable(true);
        stage.setMinWidth(1040);
        stage.setHeight(620);

        Scene newScene = new Scene(window3);
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(newScene);
        stage.centerOnScreen();

        con.setUsernameLabel(username);
        con.setImageLabel(picture);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imagePicker.getSelectionModel().selectFirst();
        selectedPicture.textProperty().bind(imagePicker.getSelectionModel().selectedItemProperty());
        selectedPicture.setVisible(false);


        imagePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldPicture, String newPicture) {
                if (oldPicture != null) {
                    switch (oldPicture) {
                        case "Default":
                            Defaultview.setVisible(false);
                            break;
                        case "Dominic":
                            Dominicview.setVisible(false);
                            break;
                        case "Sarah":
                            Sarahview.setVisible(false);
                            break;
                    }
                }
                if (newPicture != null) {
                    switch (newPicture) {
                        case "Default":
                            Defaultview.setVisible(true);
                            break;
                        case "Dominic":
                            Dominicview.setVisible(true);
                            break;
                        case "Sarah":
                            Sarahview.setVisible(true);
                            break;
                    }
                }
            }
        });
        int numberOfSquares = 30;
        while (numberOfSquares > 0){
            generateAnimation();
            numberOfSquares--;
        }
    }

    public void generateAnimation(){
        Random rand = new Random();
        int size = rand.nextInt(50) + 1;

        Rectangle r1 = new Rectangle(0,0,size,size);
        r1.setFill(Color.web("#F89406"));
        r1.setOpacity(0.1);

        KeyValue xval = null;
        if (size  % 2 == 0){
            xval = new KeyValue(r1.xProperty(), 420);
        } else {
            xval = new KeyValue(r1.xProperty(), 420);
        }

        KeyValue yval = null;
        if (size  % 3 == 0){
            yval = new KeyValue(r1.yProperty(), 420);
        } else {
            yval = new KeyValue(r1.yProperty(), 420);
        }

        rand = new Random();
        int speed = rand.nextInt(10) + 5;

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speed * 1000), xval, yval);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        borderPane.getChildren().add(borderPane.getChildren().size()-1,r1);

    }

    public void closeSystem(){
        Platform.exit();
    }
}

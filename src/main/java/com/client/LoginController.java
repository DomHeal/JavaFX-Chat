package com.client;

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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
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

    public void loginButtonAction() throws IOException {
        String hostname = hostnameTextfield.getText();
        int port = Integer.parseInt(portTextfield.getText());
        String username = usernameTextfield.getText();

        FXMLLoader y = new FXMLLoader(getClass().getResource("/styles/maindesign.fxml"));
        Parent window3 = (Pane) y.load();
        con = y.<ChatController>getController();
        Listener listener = new Listener(hostname, port, username, con);
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
        con.setImageLabel(selectedPicture.getText());

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
    }
}

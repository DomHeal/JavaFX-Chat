package com.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Dominic on 24-Oct-15.
 */
public class MainInterface{
    private MainInterfaceController controller;

    public MainInterfaceController getController() {
        return controller;
    }

    MainInterface(Stage stage, String hostname, int port, String username) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("styles/maindesign.fxml"));
        BorderPane root = (BorderPane)fxmlLoader.load();
        controller = fxmlLoader.getController();
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("styles/maindesign.fxml"));

        Scene mainScene = new Scene(root, 1020, 650);
        mainScene.setRoot(root);
        stage.setScene(mainScene);

        new Listener(hostname, port, username).start();

    }
}

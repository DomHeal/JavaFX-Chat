package com.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Dominic on 24-Oct-15.
 */
public class MainInterface{

    MainInterface(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("maindesign.fxml"));
        Scene mainScene = new Scene(root, 600, 400);
        mainScene.setRoot(root);
        stage.setScene(mainScene);
    }


}

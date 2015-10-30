package com.client;

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
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("styles/maindesign.fxml"));
        Scene mainScene = new Scene(root, 1020, 620);
        mainScene.setRoot(root);
        stage.setScene(mainScene);
    }


}

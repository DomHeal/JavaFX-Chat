package com.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Dominic on 24-Oct-15.
 */
public class MainInterface extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("maindesign.fxml"));
        primaryStage.setTitle("Socket Chat : Client version 0.1");
        Scene mainScene = new Scene(root, 1024, 720);
        mainScene.setRoot(root);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setResizable(false);

    }
}

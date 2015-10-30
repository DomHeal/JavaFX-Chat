package com.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("styles/initialdesign.fxml"));
        primaryStage.setTitle("Socket Chat : Client version 0.2");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/plug.png").toString()));

        Scene mainScene = new Scene(root, 390, 252);

        mainScene.setRoot(root);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setResizable(false);


    }


    public static void main(String[] args) {
        launch(args);
    }
}

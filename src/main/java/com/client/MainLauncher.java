package com.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainLauncher extends Application {
    public static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("styles/initialdesign.fxml"));
        Parent root = (Parent) loader.load();
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("styles/initialdesign.fxml"));
        primaryStage.setTitle("Socket Chat : Client version 0.3");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/plug.png").toString()));
        controller = loader.getController();
        setController(controller);
        Scene mainScene = new Scene(root, 420, 420);
        mainScene.setRoot(root);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(e -> Platform.exit());

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public static Controller getController(){
        return controller;
    }
}

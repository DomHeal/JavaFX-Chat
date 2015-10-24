package com;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public void loginButtonAction(){
        System.out.println("pressed");
    }

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/socketchat.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }
}

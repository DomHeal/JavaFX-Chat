package com.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Dominic on 30-Oct-15.
 */
public class MainInterfaceController implements Initializable{
    @FXML
    private TextArea messageBox;
    @FXML
    private TextArea chatFlow;

    public void sendButtonAction() {
        String msg = messageBox.getText();
        messageBox.setText("");
        if (chatFlow == null){
            System.out.println("null");
        }
        addToChat(Controller.getUsername() + ": " + msg + "\n");
                Listener.send(msg);

    }

    public void addToChat(String msg) {
        try {
            chatFlow.appendText(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatFlow.setText("");
    }

    public void print(String line) {
        System.out.println("worked");
    }
}

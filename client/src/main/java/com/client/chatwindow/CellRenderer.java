package com.client.chatwindow;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Dominic on 04-Nov-15.
 */
class CellRenderer extends ListCell<String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if(empty) {
            setGraphic(null);
            setText(null);
        }
        if (item != null) {
            ImageView imageView = new ImageView();
            Image image = new Image(getClass().getClassLoader().getResource("images/ .png").toString(),50,50,false,false);
            setText(item);
            imageView.setImage(image);
            setGraphic(imageView);

        }
    }
}
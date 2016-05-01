//package com.client.chatwindow;
//
//import com.messages.User;
//import javafx.scene.control.ListCell;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
///**
// * Created by Dominic on 04-Nov-15.
// */
//class StatusCellRenderer extends ListCell<String> {
//    @Override
//    public void updateItem(User item, boolean empty) {
//        super(updateItem(item, empty));
//        if(empty) {
//            setGraphic(null);
//            setText(null);
//        }
//        if (item != null) {
//            ImageView imageView = new ImageView();
//            Image image = new Image(getClass().getClassLoader().getResource("images/online.png").toString(),16,16,false,false);
//            imageView.setImage(image);
//            setGraphic(imageView);
//
//        }
//    }
//}
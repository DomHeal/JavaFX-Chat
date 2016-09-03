package com.client.chatwindow;

import com.messages.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * A Class for Rendering users images / name on the userlist.
 */
class CellRenderer implements Callback<ListView<User>,ListCell<User>>{
        @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<User>(){

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    setText(user.getName());
                    ImageView imageView = new ImageView();
                    Image image = new Image(getClass().getClassLoader().getResource("images/" + user.getPicture() + ".png").toString(),50,50,false,false);
                    setText(user.getName());
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        };
        return cell;
    }
}
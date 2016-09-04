package com.client.chatwindow;

import com.messages.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

;

/**
 * This class is for rendering for status of the clients - this is for future development
 */
class StatusCellRenderer implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> p) {
        ListCell<User> cell = new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                if (bln) {
                    setGraphic(null);
                    setText(null);
                }
                if (user != null) {
                    ImageView imageView = new ImageView();
                    Image image = new Image(getClass().getClassLoader().getResource("images/" + user.getStatus() + ".png").toString(), 16, 16, false, false);
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        };
        return cell;
    }
}
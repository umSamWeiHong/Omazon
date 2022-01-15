package main.java.gui.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.File;

public class SlideMenuController {

    @FXML
    public ListView<HBox> list;
    private Timeline openMenu, closeMenu;
    private boolean isShown = true;

    @FXML
    public void initialize() {
        ObservableList<HBox> boxes = FXCollections.observableArrayList();
        boxes.add(setHBox("account", "My Account"));
        boxes.add(setHBox("store", "My Store"));
        boxes.add(setHBox("cart", "My Cart"));
        boxes.add(setHBox("order", "My Order"));
        boxes.add(setHBox("favourite", "My Favourite"));
        boxes.add(setHBox("setting", "Settings"));
        list.setItems(boxes);
        setSlideTransition();
    }

    private HBox setHBox(String imageName, String text) {
        HBox box = new HBox();
        ImageView imageView = new ImageView(new Image(new File(
                "src/main/resources/img/slidemenu/" + imageName + ".png").toURI().toString()));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        box.getChildren().add(imageView);
        box.getChildren().add(new Label(text));
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    public void setSlideTransition() {
        KeyValue openValue = new KeyValue(list.prefWidthProperty(), 0);
        KeyValue closeValue = new KeyValue(list.prefWidthProperty(), 150);
        KeyFrame openFrame = new KeyFrame(Duration.millis(500), openValue);
        KeyFrame closeFrame = new KeyFrame(Duration.millis(500), closeValue);

        openMenu = new Timeline(closeFrame, openFrame);
        closeMenu = new Timeline(openFrame, closeFrame);
    }

    public void slide() {
        if (isShown)
            closeMenu.play();
        else
            openMenu.play();
        isShown = !isShown;
    }
}

package main.java.gui.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.java.gui.MainGUI;
import main.java.gui.Page;

public class SlideMenuController {

    @FXML private VBox slideMenu;
    @FXML private Button profile, store, cart, order, favourite, settings;

    private Timeline openMenu, closeMenu;
    private boolean isShown = true;

    @FXML
    public void initialize() {
        setSlideTransition();
        setOnClickAction();
    }

    private void setOnClickAction() {
        profile.setOnAction(e -> MainGUI.loadScene(Page.PROFILE));
        store.setOnAction(e -> MainGUI.loadScene(Page.STORE));
        cart.setOnAction(e -> MainGUI.loadScene(Page.CART));
        order.setOnAction(e -> MainGUI.loadScene(Page.ORDER));
        favourite.setOnAction(e -> MainGUI.loadScene(Page.FAVOURITE));
        settings.setOnAction(e -> MainGUI.loadScene(Page.SETTINGS));
    }

    public void setSlideTransition() {
        KeyValue openValue = new KeyValue(slideMenu.prefWidthProperty(), 0);
        KeyValue closeValue = new KeyValue(slideMenu.prefWidthProperty(), 150);
        KeyValue leftValue = new KeyValue(slideMenu.translateXProperty(), -40);
        KeyValue rightValue = new KeyValue(slideMenu.translateXProperty(), 0);
        KeyFrame openFrame = new KeyFrame(Duration.millis(500), openValue, leftValue);
        KeyFrame closeFrame = new KeyFrame(Duration.millis(500), closeValue, rightValue);

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

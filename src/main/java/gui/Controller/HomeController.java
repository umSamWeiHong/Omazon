package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.Main;
import main.java.User;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.io.File;

public class HomeController {

    private static User user;

    @FXML Button exploreButton, notificationButton, logoutButton;
    @FXML Button profile, store, cart, order, favourite, settings;
    @FXML ImageView logo, explore, notification, logout;

    @FXML Label username;

    private static Image exploreStatic, exploreAnimation,
            logoutStatic, logoutAnimation,
            notificationStatic, notificationAnimation;

    @FXML
    public void initialize() {

        user = Main.getUser();

        setMenuBarButtonImage();
        setOnClickAction();

        username.setText(user.getUsername().toUpperCase() + "!");
    }

    private void setMenuBarButtonImage() {
        exploreStatic = new Image(new File("src/main/resources/img/menubar/explore_static.png").toURI().toString());
        exploreAnimation = new Image(new File("src/main/resources/img/menubar/explore_anim.gif").toURI().toString());
        logoutStatic = new Image(new File("src/main/resources/img/menubar/logout_static.png").toURI().toString());
        logoutAnimation = new Image(new File("src/main/resources/img/menubar/logout_anim.gif").toURI().toString());
        notificationStatic = new Image(new File("src/main/resources/img/menubar/notification_static.png").toURI().toString());
        notificationAnimation = new Image(new File("src/main/resources/img/menubar/notification_anim.gif").toURI().toString());

        exploreButton.setOnMouseEntered(e -> explore.setImage(exploreAnimation));
        exploreButton.setOnMouseExited(e -> explore.setImage(exploreStatic));
        logoutButton.setOnMouseEntered(e -> logout.setImage(logoutAnimation));
        logoutButton.setOnMouseExited(e -> logout.setImage(logoutStatic));
        notificationButton.setOnMouseEntered(e -> notification.setImage(notificationAnimation));
        notificationButton.setOnMouseExited(e -> notification.setImage(notificationStatic));
    }

    private void setOnClickAction() {
        logo.setOnMouseClicked(e -> MainGUI.loadScene(Page.HOME));
        logoutButton.setOnMouseClicked(e -> MainGUI.loadScene(Page.LOGIN));
        profile.setOnAction(e -> MainGUI.loadScene(Page.PROFILE));
        store.setOnAction(e -> MainGUI.loadScene(Page.STORE));
        cart.setOnAction(e -> MainGUI.loadScene(Page.CART));
        order.setOnAction(e -> MainGUI.loadScene(Page.ORDER));
        favourite.setOnAction(e -> MainGUI.loadScene(Page.FAVOURITE));
        settings.setOnAction(e -> MainGUI.loadScene(Page.SETTINGS));
    }
}

package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.io.File;

public class MenuBarController {

    @FXML Button exploreButton, notificationButton, logoutButton;
    @FXML ImageView explore, notification, logout;

    private static Image exploreStatic, exploreAnimation,
                         logoutStatic, logoutAnimation,
                         notificationStatic, notificationAnimation;

    @FXML
    public void initialize() {
        exploreStatic = new Image(new File("src/main/resources/img/explore_static.png").toURI().toString());
        exploreAnimation = new Image(new File("src/main/resources/img/explore_anim.gif").toURI().toString());
        logoutStatic = new Image(new File("src/main/resources/img/logout_static.png").toURI().toString());
        logoutAnimation = new Image(new File("src/main/resources/img/logout_anim.gif").toURI().toString());
        notificationStatic = new Image(new File("src/main/resources/img/notification_static.png").toURI().toString());
        notificationAnimation = new Image(new File("src/main/resources/img/notification_anim.gif").toURI().toString());

        exploreButton.setOnMouseEntered(e -> explore.setImage(exploreAnimation));
        exploreButton.setOnMouseExited(e -> explore.setImage(exploreStatic));
        logoutButton.setOnMouseEntered(e -> logout.setImage(logoutAnimation));
        logoutButton.setOnMouseExited(e -> logout.setImage(logoutStatic));
        notificationButton.setOnMouseEntered(e -> notification.setImage(notificationAnimation));
        notificationButton.setOnMouseExited(e -> notification.setImage(notificationStatic));

        logoutButton.setOnMouseClicked(e -> MainGUI.loadScene(Page.LOGIN));
    }
}

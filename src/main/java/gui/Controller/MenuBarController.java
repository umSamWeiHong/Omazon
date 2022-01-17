package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.io.File;

public class MenuBarController {

    @FXML private Button exploreButton, notificationButton, logoutButton, menuButton;
    @FXML private ImageView logo, explore, notification, logout;

    private static Image exploreStatic, exploreAnimation,
                         logoutStatic, logoutAnimation,
                         notificationStatic, notificationAnimation;

    private static SlideMenuController slideMenuController;

    public void setSlideMenuController(SlideMenuController slideMenuController) {
        MenuBarController.slideMenuController = slideMenuController;
    }

    @FXML
    public void initialize() {
        setMenuButtonImage();
        setOnAction();
    }

    private void setMenuButtonImage() {
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

    private void setOnAction() {
        logo.setOnMouseClicked(e -> MainGUI.loadScene(Page.HOME));
        exploreButton.setOnAction(e -> MainGUI.loadScene(Page.EXPLORE));
        notificationButton.setOnAction(e -> MainGUI.loadScene(Page.PROFILE));
        logoutButton.setOnAction(e -> MainGUI.loadScene(Page.LOGIN));
        menuButton.setOnAction(e -> slideMenuController.slide());
    }
}

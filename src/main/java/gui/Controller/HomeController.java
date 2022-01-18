package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import main.java.Main;
import main.java.Product;
import main.java.StoredDB;
import main.java.User;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.io.File;

public class HomeController extends Controller {

    private static User user;

    @FXML private GridPane gridPane;
    @FXML private Button exploreButton, notificationButton, logoutButton;
    @FXML private Button profile, store, cart, order, favourite, settings, searchButton;
    @FXML private ImageView logo, explore, notification, logout;
    @FXML private TextField searchStatement;

    @FXML private FlowPane productPane;

    @FXML Label username;

    private static Image exploreStatic, exploreAnimation,
            logoutStatic, logoutAnimation,
            notificationStatic, notificationAnimation;

    @FXML
    public void initialize() {
        setMenuBarButtonImage();
        setOnClickAction();
    }

    @Override
    public void update() {
        user = Main.getUser();
        username.setText(user.getUsername().toUpperCase() + "!");
        setBestSellingProducts();
        setTopSellingProducts();
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

    private void setBestSellingProducts() {
        StoredDB[] products = Product.getBestSelling();
        int i = 0;
        for (StoredDB item : products) {
            Product product = (Product) item;
            gridPane.add(DBNode.productButton(product), i++, 1);
        }
    }

    private void setTopSellingProducts() {
        StoredDB[] products = Product.getTopSelling();
        for (int i = 3; i < products.length; i++) {
            Product product = (Product) products[i];
            if (product == null)
                break;
            productPane.getChildren().add(DBNode.productButton(product));
        }
    }

    private void setOnClickAction() {
        logo.setOnMouseClicked(e -> MainGUI.loadScene(Page.HOME));
        exploreButton.setOnAction(e -> MainGUI.loadScene(Page.EXPLORE));
        notificationButton.setOnAction(e -> MainGUI.loadScene(Page.PROFILE));
        logoutButton.setOnMouseClicked(e -> MainGUI.loadScene(Page.LOGIN));
        profile.setOnAction(e -> MainGUI.loadScene(Page.PROFILE));
        store.setOnAction(e -> {
            StoreController.setSeller(Main.getUser());
            MainGUI.loadScene(Page.STORE);
        });
        cart.setOnAction(e -> MainGUI.loadScene(Page.CART));
        order.setOnAction(e -> MainGUI.loadScene(Page.ORDER));
        favourite.setOnAction(e -> MainGUI.loadScene(Page.FAVOURITE));
        settings.setOnAction(e -> MainGUI.loadScene(Page.SETTINGS));
        searchButton.setOnAction(e -> {
            String statement = searchStatement.getText();
            if (statement.equals(""))
                return;
            SearchController.setSearchStatement(statement);
            SearchController.setCategory(null);
            MainGUI.loadScene(Page.SEARCH);
        });
    }
}

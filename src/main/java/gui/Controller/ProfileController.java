package main.java.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.io.IOException;

public class ProfileController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private ListView<Label> orderList, reviewList, notificationList;
    @FXML private Button topUpButton, storeButton, cartButton, favouriteButton;
    @FXML private Label username, balance;

    private static User user;

    @FXML
    public void initialize() {
        topUpButton.setOnAction(e -> invokeTopUpDialog());
        storeButton.setOnAction(e -> {
            StoreController.setSeller(Main.getUser());
            MainGUI.loadScene(Page.STORE);
        });
        cartButton.setOnAction(e -> MainGUI.loadScene(Page.CART));
        favouriteButton.setOnAction(e -> MainGUI.loadScene(Page.FAVOURITE));
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        username.setText(user.getUsername());
        balance.setText(String.format("%.2f", user.getBalance()));

        StoredDB[] orders = Order.getUserOrders(user.getUserID());
        if (orders != null) {
            for (StoredDB order : orders) {
                Label label = DBNode.orderLabel((Order) order);

                orderList.getItems().add(label);
            }
        }

        StoredDB[] reviews = Review.getUserReviews(user.getUserID());
        if (reviews != null) {
            for (StoredDB review : reviews) {
                Label label = DBNode.reviewLabel((Review) review);

                reviewList.getItems().add(label);
            }
        }

        Label[] transactionLabels = DBNode.transactionLabel(user.getUserID());
        if (transactionLabels != null) {
            for (Label label : transactionLabels)
                notificationList.getItems().add(label);
        }
    }
    
    public void invokeTopUpDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Top Up");

        ButtonType TOP_UP = new ButtonType("Top Up", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(TOP_UP);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        HBox box = new HBox();
        Label text = new Label("Enter the amount: ");
        TextField amountField = new TextField();
        box.getChildren().add(text);
        box.getChildren().add(amountField);
        
        dialog.getDialogPane().setContent(box);
        dialog.setResizable(true);

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(TOP_UP);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            
            double amount = Double.parseDouble(amountField.getText());
                        
            if (amount < 0) {
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == TOP_UP) {
                double sum = Double.parseDouble(balance.getText()) + Double.parseDouble(amountField.getText());
                balance.setText(String.format("%.2f", sum));
                user.setBalance(sum);
            }
        });
    }
}

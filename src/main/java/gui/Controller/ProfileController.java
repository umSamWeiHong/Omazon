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

import java.io.IOException;

public class ProfileController {

    @FXML private BorderPane borderPane;
    @FXML private ListView<Label> orderList, reviewList;
    @FXML ScrollPane reviewScroll;
    @FXML GridPane gridPane;
    @FXML private Button logout, topUpButton;
    @FXML private Label username, balance;

    private static User user;

    @FXML
    public void initialize() {

        user = Main.getUser();
        username.setText(user.getUsername());

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        topUpButton.setOnAction(e -> invokeTopUpDialog());
        balance.setText(String.format("%.2f", user.getBalance()));

        StoredDB[] orders = Order.getUserOrders(user.getUserID());
        if (orders == null)
            return;
        else {
            for (StoredDB order : orders) {
                Label label = DBNode.orderLabel((Order) order);

                orderList.getItems().add(label);
            }
        }

        StoredDB[] reviews = Review.getUserReviews(user.getUserID());
        if (reviews == null)
            return;
        else {
            for (StoredDB review : reviews) {
                Label label = DBNode.reviewLabel((Review) review);

                reviewList.getItems().add(label);
            }
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
//                controller.setMessageText(message);
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == TOP_UP) {
                double sum = Double.parseDouble(balance.getText()) + Double.parseDouble(amountField.getText());
                balance.setText(String.format("%.2f", sum));
                user.topUp(sum);
            }
        });
    }
}

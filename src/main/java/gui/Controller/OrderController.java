package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

public class OrderController {

    @FXML private BorderPane borderPane;
    @FXML private FlowPane list;

    private static User user;

    @FXML
    public void initialize() {

        user = Main.getUser();

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        StoredDB[] orders = Order.getUserOrders(user.getUserID());
        if (orders == null)
            return;
        else {
            for (StoredDB order : orders) {

                Label label = DBNode.orderLabel((Order) order);

                list.getChildren().add(label);
            }
        }
    }
}

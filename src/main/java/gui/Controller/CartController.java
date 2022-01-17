package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

public class CartController {

    @FXML private BorderPane borderPane;
    @FXML private FlowPane list;

    private static User user;

    @FXML
    public void initialize() {

        user = Main.getUser();

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        StoredDB[] cartItems = Cart.getCartItems(user.getUserID());
        if (cartItems != null) {
            int N = cartItems.length;
            int[] productIDs = new int[N];
            for (int i = 0; i < N; i++)
                productIDs[i] = ((Cart) cartItems[i]).getProductID();
            Product[] products = Product.getProductsByProductIDs(productIDs);

            for (Product product : products) {
                if (product == null)
                    continue;
                try {
                    Button button = DBNode.productButton(product);
                    list.getChildren().add(button);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
    }
}

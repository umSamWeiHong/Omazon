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
            for (StoredDB cartItem : cartItems) {
                int productID = ((Cart) cartItem).getProductID();
                Product product;
                try {
                    product = new Product(productID);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                Button button = DBNode.cartButton((Cart) cartItem, product);
                list.getChildren().add(button);
            }
//            Product[] products = Product.getProductsByProductIDs(productIDs);
        }
    }
}

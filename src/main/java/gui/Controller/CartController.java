package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

public class CartController {

    @FXML private BorderPane borderPane;
    @FXML private VBox vBox;
    @FXML private FlowPane list;

    private static double amount;
    private static User user;
    private static StoredDB[] cartItems;

    @FXML
    public void initialize() {

        user = Main.getUser();

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        cartItems = Cart.getCartItems(user.getUserID());
        if (cartItems == null)
            return;

        for (StoredDB cartItem : cartItems) {
            Cart cart = (Cart) cartItem;
            int productID = cart.getProductID();
            Product product;
            try {
                product = new Product(productID);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }
            Button button = DBNode.cartButton(cart, product);
            list.getChildren().add(button);
        }

        amount = Cart.getTotalAmount(cartItems);
        vBox.getChildren().add(setCheckoutBar());
    }

    private HBox setCheckoutBar() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        Text text = new Text("Total price: RM ");
        Label totalPrice = new Label();
        Button button = new Button("Checkout");

        totalPrice.setPrefWidth(400);
        totalPrice.setText(amount + "");

        box.getChildren().add(text);
        box.getChildren().add(totalPrice);
        box.getChildren().add(button);
        return box;
    }
}

package main.java.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.util.ArrayList;
import java.util.HashMap;

public class CartController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private VBox vBox;
    @FXML private FlowPane list;

    private static double amount;
    private static User user;
    private static StoredDB[] cartItems;

    private static HashMap<Integer, Button> map = new HashMap<>();

    @FXML
    public void initialize() {

//        user = Main.getUser();

//        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
//        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());
//
//        cartItems = Cart.getCartItems(user.getUserID());
//        if (cartItems == null)
//            return;
//
//        for (StoredDB cartItem : cartItems) {
//            Cart cart = (Cart) cartItem;
//            int productID = cart.getProductID();
//            Product product;
//            try {
//                product = new Product(productID);
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
//                continue;
//            }
//            Button button = DBNode.cartButton(cart, product);
//            list.getChildren().add(button);
//        }
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        user.updateCartIDs();
        int[] ids = user.getCartIDs();
        updateMap(ids);

        //amount = Cart.getTotalAmount(map.values());
        vBox.getChildren().add(setCheckoutBar());
    }

    private void updateMap(int[] ids) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i : map.keySet())
            if (!Main.linearSearch(ids, i))
                toRemove.add(i);

        for (int i : toRemove) {
            list.getChildren().remove(map.get(i));
            map.remove(i);
        }

        for (int id : ids)
            if (!map.containsKey(id)) {
                try {
                    Button button = DBNode.cartButton(id);
                    list.getChildren().add(button);
                    map.put(id, button);
                } catch (IllegalArgumentException e) {
                    System.out.println("CartController: ProductID not found.");
                }
            }
    }

    private HBox setCheckoutBar() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        Text text = new Text("Total price: RM ");
        Label totalPrice = new Label();
        Button button = new Button("Checkout");

        totalPrice.setPrefWidth(400);
        totalPrice.setText(String.format("%.2f", amount));

        box.getChildren().add(text);
        box.getChildren().add(totalPrice);
        box.getChildren().add(button);

        button.setOnAction(e -> checkout());

        return box;
    }

    private void checkout() {
        if (user.getBalance() < amount) {
            invokeTopUpDialog();
            return;
        }

        for (StoredDB item : cartItems) {
            Cart cart = (Cart) item;
            Product product = new Product(cart.getProductID());
            Order order = new Order(cart.getProductID(), user.getUserID(), product.getSellerID(), user.getShippingAddress(), cart.getQuantity());
            product.setStock(product.getStock() - order.getOrderQuantity());
            Database.updateDatabase(cart.deleteQuery());
            Database.updateDatabase(order.insertQuery());
        }
        user.setBalance(user.getBalance() - amount);
        vBox.getChildren().clear();
        amount = 0;
    }

    private void invokeTopUpDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Top Up");
        dialog.setResizable(true);

        ButtonType TOP_UP = new ButtonType("Top Up Now", ButtonBar.ButtonData.OK_DONE);
        ButtonType CLOSE = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(TOP_UP);
        dialog.getDialogPane().getButtonTypes().add(CLOSE);

        dialog.setContentText("Not enough money");

        dialog.showAndWait().ifPresent(response -> {
            if (response == TOP_UP) {
                MainGUI.loadScene(Page.PROFILE);
            }
        });
    }
}

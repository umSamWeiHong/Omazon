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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class CartController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private VBox vBox;
    @FXML private FlowPane list;

    private Label totalPrice;

    private static double amount;
    private static User user;

    private static final HashMap<Integer, Button> map = new HashMap<>();

    @FXML
    public void initialize() {
        vBox.getChildren().add(setCheckoutBar());
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        user.updateCartIDs();
        int[] ids = user.getCartIDs();
        updateMap(ids);

        amount = Cart.getTotalAmount(user.getUserID());
        totalPrice.setText(String.format("%.2f", amount));
    }

    private void updateMap(int[] ids) {

        if (ids == null)
            return;

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
                    list.getChildren().add(0, button);
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
        totalPrice = new Label();
        Button button = new Button("Checkout");

        totalPrice.setPrefWidth(400);

        box.getChildren().add(text);
        box.getChildren().add(totalPrice);
        box.getChildren().add(button);

        button.setOnAction(e -> {
            if (user.getBalance() < amount) {
                invokeTopUpDialog();
                return;
            }
            invokeEnterPasswordDialog();
        });

        return box;
    }

    private void checkout() {

        if (amount == 0)
            return;

        for (int cartID : map.keySet()) {
            Cart cart = new Cart(cartID);
            Product product = new Product(cart.getProductID());
            Order order = new Order(cart.getProductID(), user.getUserID(), product.getSellerID(), user.getShippingAddress(), cart.getQuantity());
            product.setStock(product.getStock() - order.getOrderQuantity());
            Database.updateDatabase(order.insertQuery());

            String query = String.format("""
                    SELECT orderID FROM `Order`
                     WHERE productID = %d AND userID = %d AND sellerID = %d
                     ORDER BY orderTime DESC LIMIT 1""",
                    cart.getProductID(), user.getUserID(), product.getSellerID());
            ResultSet resultSet = Database.queryDatabase(query);

            User seller = new User(product.getSellerID());
            seller.setProfit(seller.getProfit() + order.getOrderQuantity() * product.getPrice());
            Database.updateDatabase(seller.updateQuery());

            try {
                resultSet.next();
                int orderID = resultSet.getInt("orderID");
                Transaction transaction = new Transaction(product.getSellerID(), user.getUserID(), orderID, order.getOrderQuantity() * product.getPrice());
                Database.updateDatabase(transaction.insertQuery());

            } catch (SQLException e) {
                e.printStackTrace();
            }
            Database.updateDatabase(product.updateQuery());
            Database.updateDatabase(cart.deleteQuery());
        }
        user.setBalance(user.getBalance() - amount);
        list.getChildren().clear();
        amount = 0;
        totalPrice.setText("0.00");
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

    private void invokeEnterPasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Enter Payment Password");
        dialog.setResizable(true);

        ButtonType CONFIRM = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType CLOSE = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(CONFIRM);
        dialog.getDialogPane().getButtonTypes().add(CLOSE);

        Label label = new Label("Enter payment password: ");
        TextField passwordField = new TextField();
        Label message = new Label();

        GridPane gridPane = new GridPane();
        gridPane.add(label, 0, 0);
        gridPane.add(passwordField, 1, 0);
        gridPane.add(message, 0, 1, 2, 1);
        GridPane.setHalignment(message, HPos.CENTER);

        dialog.getDialogPane().setGraphic(gridPane);

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(CONFIRM);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            String password = passwordField.getText();
            if (!password.equals(user.getPaymentPassword())) {
                message.setText("Invalid password.");
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == CONFIRM) {
                System.out.println("Password correct.");
                checkout();
            }
        });
    }
}

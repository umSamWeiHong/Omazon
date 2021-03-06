package main.java.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.util.ArrayList;
import java.util.HashMap;


public class StoreController extends Controller {

    private static User seller;

    @FXML private BorderPane borderPane;
    @FXML private FlowPane productPane;
    @FXML private Button profileButton, transactionButton, addProductButton;
    @FXML private Label username, profit;

    private static final HashMap<Integer, Button> map = new HashMap<>();

    @FXML
    public void initialize() {
        productPane.setVgap(10);

        addProductButton.setOnAction(e -> invokeAddProductDialog());
        transactionButton.setOnAction(e -> invokeTransactionHistoryDialog(seller.getUserID()));
        profileButton.setOnAction(e -> MainGUI.loadScene(Page.PROFILE));
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        username.setText(seller.getUsername());

        if (seller.getUserID() == Main.getUser().getUserID())
            profit.setText("Profit: " + seller.getProfit());
        else
            profit.setText("");
        seller.updateProductIDs();
        int[] ids = seller.getProductIDs();
        updateMap(ids);
    }

    private void updateMap(int[] ids) {

        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i : map.keySet())
            if (!Main.linearSearch(ids, i))
                toRemove.add(i);

        for (int i : toRemove) {
            productPane.getChildren().remove(map.get(i));
            map.remove(i);
        }

        for (int id : ids)
            if (!map.containsKey(id)) {
                try {
                    Button button = DBNode.productButton(id);
                    productPane.getChildren().add(button);
                    map.put(id, button);
                } catch (IllegalArgumentException e) {
                    System.out.println("StoreController: ProductID not found.");
                }
            }
    }

    private void invokeAddProductDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Product");

        ButtonType ADD = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(ADD);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        FXMLLoader dialogContentLoader = MainGUI.getLoaderWithNode("AddProductDialog");

        dialog.getDialogPane().setContent(dialogContentLoader.getRoot());
        dialog.setResizable(true);

        AddProductDialogController controller = dialogContentLoader.getController();

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(ADD);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {

            if (controller.getSelectedCategory() == null) {
                controller.setMessageText("Please select a category.");
                event.consume();
            }

            String message = Product.validateProductInformation(controller.getName(), controller.getPrice(), controller.getStock());

            if (!message.equals("OK")) {
                controller.setMessageText(message);
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == ADD) {
                Product product = new Product(
                        controller.getName(),
                        controller.getDescription(),
                        Double.parseDouble(controller.getPrice()),
                        Integer.parseInt(controller.getStock()),
                        Category.valueOf(controller.getSelectedCategory().toUpperCase().replace(" ", "_")),
                        MainGUI.encode(controller.getImage()),
                        Main.getUser().getUserID());
                Database.updateDatabase(product.insertQuery());
                MainGUI.loadScene(Page.STORE);
            }
        });
    }

    private void invokeTransactionHistoryDialog(int sellerID) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Transaction History");

        ButtonType DONE = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(DONE);

        ListView<Label> list = new ListView<>();
        Label[] labels = DBNode.transactionLabel(sellerID);

        dialog.getDialogPane().setContent(list);
        dialog.setResizable(true);

        if (labels == null)
            return;

        for (Label label : labels) {
            list.getItems().add(label);
        }

        dialog.showAndWait();
    }

    public static void setSeller(User seller) {
        StoreController.seller = seller;
    }
}

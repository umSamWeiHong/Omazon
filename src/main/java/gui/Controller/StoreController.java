package main.java.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

import java.util.ArrayList;
import java.util.HashMap;


public class StoreController extends Controller {

    private static User user;

    @FXML private BorderPane borderPane;
    @FXML private FlowPane productPane;
    @FXML private Button button;

    private static final HashMap<Integer, Button> map = new HashMap<>();

    @FXML
    public void initialize() {
        productPane.setVgap(10);

        button.setOnMouseClicked(e -> invokeAddProductDialog());
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        user.updateProductIDs();
        int[] ids = user.getProductIDs();
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
                        Main.getUser().getUserID());
                System.out.println(product);
                Database.updateDatabase(product.insertQuery());
            }
        });
    }
}

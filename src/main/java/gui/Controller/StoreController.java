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
import main.java.Product;
import main.java.Review;
import main.java.StoredDB;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

public class StoreController {

    @FXML private BorderPane borderPane;
    @FXML private FlowPane productPane;
    @FXML private Button button;


    @FXML
    public void initialize() {

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        productPane.setVgap(10);
        productPane.getChildren().add(DBNode.productButton(3));
        productPane.getChildren().add(DBNode.orderLabel(3));
        productPane.getChildren().add(DBNode.productButton(4));
        productPane.getChildren().add(DBNode.reviewLabel(8));

        for (StoredDB r : Review.getProductReviews(3))
            productPane.getChildren().add(DBNode.reviewLabel(((Review) r).getReviewID()));

        button.setOnMouseClicked(e -> invokeAddProductDialog());

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
                        controller.getSelectedCategory());
                System.out.println(product);
            }
        });
    }
}

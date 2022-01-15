package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.java.Product;
import main.java.Review;
import main.java.StoredDB;
import main.java.gui.MainGUI;

public class ProductController {

    private static int productID;

    @FXML BorderPane borderPane;
    @FXML VBox vBox;
    @FXML
    private Label name, seller, rating, reviewCount, price, sales, stock,
            bestSelling, shippingFee, totalPrice, description;

    @FXML
    public void initialize() {

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        setProductInformation(productID);

        for (StoredDB r : Review.getProductReviews(3, 5)) {
            vBox.getChildren().add(setLabel((Review) r));
        }
    }

    // TODO Waiting for methods
    private void setProductInformation(int productID) {
        Product product = new Product(productID);
        bestSelling.setText(product.isBestSelling() ? "Best Selling" : "");
        name.setText(product.getProductName());
        seller.setText("from " + "SELLER");
        rating.setText(String.format("Rating: %.2f (%d reviews)", 0.0, 0));
        price.setText("Price: RM " + product.getPrice());
        shippingFee.setText("Shipping Fee: RM " + "0.0");
        totalPrice.setText("Total Price: RM" + product.getPrice());
        sales.setText("Sales: " + product.getSales());
        stock.setText("Stock: " + product.getStock());
        description.setText(product.getDescription());

        name.setWrapText(true);
        description.setWrapText(true);

        MainGUI.stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            name.setPrefWidth(MainGUI.stage.getWidth() - 20);
            description.setPrefWidth(MainGUI.stage.getWidth() - 20);
        });
    }

    public Label setLabel(Review review) {
        Label label = new Label();
        GridPane labelPane = new GridPane();
        label.setMinWidth(vBox.getPrefWidth());
        label.setMinHeight(100);
        label.setGraphic(labelPane);

        Label rating = new Label("" + review.getRating());
        Label subject = new Label(review.getSubject());
        Label description = new Label(review.getDescription());
        Label datetime = new Label(review.getDatetime().toString());

        labelPane.add(rating, 1, 0);
        labelPane.add(subject, 1, 1);
        labelPane.add(description, 1, 2);
        labelPane.add(datetime, 1, 3);

        return label;
    }

    public static void setProductID(int productID) {
        ProductController.productID = productID;
    }
}

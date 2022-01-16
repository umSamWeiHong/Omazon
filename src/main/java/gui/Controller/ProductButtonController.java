package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.Product;

public class ProductButtonController {

    @FXML private Label name, price, rating;

    @FXML
    public void initialize() {
        Product product = new Product(5);
        setText(product);
    }

    public void setText(Product product) {
        name.setText(product.getProductName());
        price.setText(product.getPrice() + "");
        // TODO Change ratings
        rating.setText("5.00");
    }
}

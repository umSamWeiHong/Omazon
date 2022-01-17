package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.Category;
import main.java.Product;

public class EditProductDialogController {

    @FXML private Label message;
    @FXML private TextArea name, description;
    @FXML private TextField price, stock;
    @FXML private MenuButton category;

    private Product product;
    private String selectedCategory;

    @FXML
    public void initialize() {

        for (Category c : Category.values()) {
            MenuItem item = new MenuItem(c.getDisplayName());
            item.setOnAction(e -> setSelectedCategory(c.getDisplayName()));
            category.getItems().add(item);
        }
    }

    public void setText() {
        name.setText(product.getProductName());
        category.setText(product.getCategory().getDisplayName());
        price.setText(String.format("%.2f", product.getPrice()));
        stock.setText(product.getStock() + "");
        description.setText(product.getDescription());
        selectedCategory = product.getCategory().toString();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private void setSelectedCategory(String selectedCategory) {
        category.setText(selectedCategory);
        this.selectedCategory = selectedCategory.toUpperCase().replace(" ", "_");
    }

    public void setMessageText(String text) {
        message.setText(text);
    }

    public String getName() {
        return name.getText();
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public String getPrice() {
        return price.getText();
    }

    public String getStock() {
        return stock.getText();
    }

    public String getDescription() {
        return description.getText();
    }
}

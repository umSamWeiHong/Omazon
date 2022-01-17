package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.java.Category;

public class AddProductDialogController {

    @FXML private Label message;
    @FXML private TextArea name, description;
    @FXML private TextField price, stock;
    @FXML private MenuButton category;

    private String selectedCategory;

    @FXML
    public void initialize() {

        for (Category c : Category.values()) {
            MenuItem item = new MenuItem(c.getDisplayName());
            item.setOnAction(e -> setSelectedCategory(c.getDisplayName()));
            category.getItems().add(item);
        }
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

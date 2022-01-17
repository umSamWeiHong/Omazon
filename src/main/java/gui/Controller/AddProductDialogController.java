package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import main.java.Category;
import main.java.gui.MainGUI;

import java.io.File;

public class AddProductDialogController {

    @FXML private Label message;
    @FXML private TextArea name, description;
    @FXML private TextField price, stock;
    @FXML private MenuButton category;
    @FXML private Button chooseImageButton;

    private String selectedCategory;
    private File image;

    @FXML
    public void initialize() {
        for (Category c : Category.values()) {
            MenuItem item = new MenuItem(c.getDisplayName());
            item.setOnAction(e -> setSelectedCategory(c.getDisplayName()));
            category.getItems().add(item);
        }

        chooseImageButton.setOnAction(e -> setImage());
    }

    private void setImage() {
        FileChooser fileChooser = new FileChooser();
        image = fileChooser.showOpenDialog(MainGUI.getStage());
        if (image == null)
            return;
        chooseImageButton.setText(image.getName());
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

    public File getImage() {
        return image;
    }

    public String getDescription() {
        return description.getText();
    }
}

package main.java.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import main.java.Product;
import main.java.gui.Controller.ProductController;

import java.io.File;

public class DBNode {

    public static Button productButton(int productID) {
        Button button = new Button();
        button.setPrefWidth(250);
        button.setPrefHeight(100);
        button.setMaxHeight(150);
        button.getStylesheets().add(new File("src/main/resources/css/ProductButton.css").toURI().toString());

        GridPane gridPane = new GridPane();
        Label name = new Label("NAME");
        Label price = new Label("PRICE");
        Label rating = new Label("RATING");

        Product product = new Product(productID);

        button.setGraphic(gridPane);

        name.setWrapText(true);
        name.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");

        Image image = MainGUI.decode(product.getBase64String());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(75);
        gridPane.add(imageView, 0, 0, 1, 2);
        gridPane.add(name, 1, 0, 2, 1);
        gridPane.add(price, 1, 1);
        gridPane.add(rating, 2, 1);
        GridPane.setMargin(imageView, new Insets(0, 5, 0, 0));
        GridPane.setMargin(price, new Insets(5, 0, 0, 0));
        GridPane.setMargin(rating, new Insets(5, 0, 0, 0));
        GridPane.setHalignment(rating, HPos.RIGHT);
        GridPane.setValignment(imageView, VPos.CENTER);
        GridPane.setValignment(price, VPos.BOTTOM);
        GridPane.setValignment(rating, VPos.BOTTOM);

        name.setText(product.getProductName());
        price.setText(String.format("RM %.2f", product.getPrice()));
        // TODO Change ratings
        rating.setText("5.00");

        button.setOnMouseClicked(e -> {
            ProductController.setProductID(productID);
            MainGUI.loadScene(Page.PRODUCT);
        });


        return button;
    }


}

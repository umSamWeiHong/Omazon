package main.java.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import main.java.Cart;
import main.java.Order;
import main.java.Product;
import main.java.Review;
import main.java.gui.Controller.ProductController;

import java.io.File;

public class DBNode {

    private static final String cssPath = new File("src/main/resources/css/ProductButton.css").toURI().toString();

    public static Button cartButton(int cartID) {
        Cart cart = new Cart(cartID);
        return cartButton(cart, new Product(cart.getProductID()));
    }

    public static Button cartButton(Cart cart, Product product) {
        Button button = new Button();
        button.setPrefWidth(250);
        button.setPrefHeight(100);
        button.setMaxHeight(150);
        button.getStylesheets().add(cssPath);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label("NAME");
        Label quantity = new Label("QUANTITY");
        Label totalPrice = new Label("TOTAL_PRICE");

        name.setText(product.getProductName());
        quantity.setText(cart.getQuantity() + "x");
        totalPrice.setText(String.format("RM %.2f", cart.getQuantity() * product.getPrice()));

        button.setGraphic(gridPane);

        if (product.getBase64String() != null) {
            ImageView imageView = getImageView(product);
            gridPane.add(imageView, 0, 0, 1, 2);
            GridPane.setMargin(imageView, new Insets(0, 5, 0, 0));
            GridPane.setValignment(imageView, VPos.CENTER);
        }
        gridPane.add(name, 1, 0, 2, 1);
        gridPane.add(quantity, 1, 1);
        gridPane.add(totalPrice, 2, 1);
        GridPane.setMargin(quantity, new Insets(5, 0, 0, 0));
        GridPane.setMargin(totalPrice, new Insets(5, 0, 0, 0));
        GridPane.setHalignment(totalPrice, HPos.RIGHT);
        GridPane.setValignment(quantity, VPos.BOTTOM);
        GridPane.setValignment(totalPrice, VPos.BOTTOM);

        name.setWrapText(true);
        name.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");

        button.setOnMouseClicked(e -> {
            ProductController.setProduct(product);
            MainGUI.loadScene(Page.PRODUCT);
        });

        return button;
    }

    public static Button productButton(int productID) {
        return productButton(new Product(productID));
    }

    public static Button productButton(Product product) {
        Button button = new Button();
        button.setPrefWidth(250);
        button.setPrefHeight(100);
        button.setMaxHeight(150);
        button.getStylesheets().add(cssPath);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label("NAME");
        Label price = new Label("PRICE");
        Label rating = new Label("RATING");

        button.setGraphic(gridPane);

        int offset = 0;
        ImageView imageView = getImageView(product);
        if (imageView != null) {
            gridPane.add(imageView, 0, 0, 1, 2);
            GridPane.setMargin(imageView, new Insets(0, 5, 0, 0));
            GridPane.setValignment(imageView, VPos.CENTER);
            offset = 1;
        }

        gridPane.add(name, offset, 0, 2, 1);
        gridPane.add(price, offset, 1);
        gridPane.add(rating, offset+1, 1);
        GridPane.setMargin(price, new Insets(5, 0, 0, 0));
        GridPane.setMargin(rating, new Insets(5, 0, 0, 0));
        GridPane.setHalignment(rating, HPos.RIGHT);

        GridPane.setValignment(price, VPos.BOTTOM);
        GridPane.setValignment(rating, VPos.BOTTOM);

        name.setText(product.getProductName());
        price.setText(String.format("RM %.2f", product.getPrice()));
        rating.setText(getRatingStars(product.getProductRatings()));

        name.setPrefWidth(250);
        name.setWrapText(true);
        name.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");
        rating.setStyle("-fx-text-fill: orange");

        button.setOnMouseClicked(e -> {
            ProductController.setProduct(product);
            ProductController.setProductNameLabel(name);
            ProductController.setProductPriceLabel(price);
            MainGUI.loadScene(Page.PRODUCT);
        });

        return button;
    }

    public static Label orderLabel(int orderID) {
        return orderLabel(new Order(orderID));
    }

    public static Label orderLabel(Order order) {
        Label label = getNewLabel();
        Product product = new Product(order.getProductID());

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label("NAME");
        Label quantity = new Label("QUANTITY");
        Label totalPrice = new Label("TOTAL_PRICE");
        Label orderTime = new Label("ORDER_TIME");

        label.setGraphic(gridPane);

        name.setText(product.getProductName());
        quantity.setText(order.getOrderQuantity() + "x");
        totalPrice.setText(String.format("RM %.2f", order.getOrderQuantity() * product.getPrice()));
        orderTime.setText(order.getOrderTime().toString());

        int offset = 0;
        ImageView imageView = getImageView(product);
        if (imageView != null) {
            gridPane.add(imageView, 0, 0, 1, 2);
            GridPane.setMargin(imageView, new Insets(0, 5, 0, 0));
            GridPane.setValignment(imageView, VPos.CENTER);
            offset = 1;
        }

        gridPane.add(name, offset, 0, 2, 1);
        gridPane.add(quantity, offset, 1);
        gridPane.add(totalPrice, offset+1, 1);
        gridPane.add(orderTime, offset, 2, 2, 1);
        GridPane.setMargin(orderTime, new Insets(2, 0, 0, 0));
        GridPane.setHalignment(totalPrice, HPos.RIGHT);
        GridPane.setValignment(orderTime, VPos.BOTTOM);

        name.setWrapText(true);
        name.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");
        orderTime.setStyle("-fx-font-size: 10;");

        return label;
    }

    public static Label reviewLabel(Review review) {
        Label label = getNewLabel();
        Product product = new Product(review.getProductID());

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        Label rating = new Label("RATING");
        Label subject = new Label("SUBJECT");
        Label description = new Label("DESCRIPTION");

        label.setGraphic(gridPane);

        rating.setText(review.getRatingStars());
        subject.setText(review.getSubject());
        description.setText(review.getDescription());

        ImageView imageView = getImageView(product);

        gridPane.add(imageView, 0, 0, 1, 3);
        gridPane.add(rating, 1, 0);
        gridPane.add(subject, 1, 1);
        gridPane.add(description, 1, 2);
        GridPane.setMargin(imageView, new Insets(0, 5, 0, 0));
        GridPane.setValignment(imageView, VPos.CENTER);
        GridPane.setValignment(rating, VPos.TOP);
        GridPane.setValignment(description, VPos.TOP);
        description.setPrefHeight(100);

        rating.setStyle("-fx-text-fill: orange;");
        subject.setWrapText(true);
        subject.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");
        description.setWrapText(true);
        description.setStyle("-fx-font-size: 10;");

        return label;
    }

    public static String getRatingStars(double rating) {
        int r = (int) rating;
        return "★".repeat(r) + "☆".repeat(5-r);
    }

    private static Label getNewLabel() {
        Label label = new Label();
        label.setPrefWidth(250);
        label.setPrefHeight(100);
        label.setMaxHeight(150);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPadding(new Insets(6, 6, 6, 6));
        label.getStylesheets().add(cssPath);
        label.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #ffffff, #f7f7f7, #eeeeee, #e6e6e6, #dedede);
                -fx-border-color: black;
                -fx-border-width: 1;
                -fx-border-radius: 3;
                """);
        return label;
    }

    private static ImageView getImageView(Product product) {
        Image image = MainGUI.decode(product.getBase64String());
        if (image == null)
            return null;
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(75);
        return imageView;
    }
}

package main.java.gui;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.java.*;
import main.java.gui.Controller.ProductController;
import main.java.gui.Controller.StoreController;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        ImageView imageView = getImageView(product);
        if (imageView != null) {
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

        name.setPrefWidth(250);
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

        name.setPrefWidth(250);
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

        if (!order.isRated()) {
            label.setStyle("""
                        -fx-background-color: #dedede;
                        -fx-border-color: black;
                        -fx-border-width: 1;
                        -fx-border-radius: 3""");
            label.setOnMouseClicked(e -> invokeAddReviewDialog(order, product, label));
        }

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
        if (imageView != null) {
            gridPane.add(imageView, 0, 0, 1, 3);
            GridPane.setMargin(imageView, new Insets(0, 5, 0, 0));
            GridPane.setValignment(imageView, VPos.CENTER);
        }

        gridPane.add(rating, 1, 0);
        gridPane.add(subject, 1, 1);
        gridPane.add(description, 1, 2);

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

    public static Label[] transactionLabel(int sellerID) {
        String query = String.format("""
                                SELECT User.username, Product.productName, ROUND(`Order`.orderQuantity*Product.price, 2) AS amount
                                 FROM Transaction
                                 LEFT JOIN User ON Transaction.userID=User.userID
                                 LEFT JOIN `Order` ON Transaction.orderID=`Order`.orderID
                                 LEFT JOIN `Product` ON `Order`.productID=Product.productID
                                 WHERE Transaction.sellerID = %d
                                 LIMIT 50""", sellerID);
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            return null;

        int N = 0;
        try {
            resultSet.last();
            N = resultSet.getRow();
            resultSet.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Label[] labels = new Label[N];
        int i = 0;
        try {
            while (resultSet.next()) {
                Label label = new Label();
                double amount = resultSet.getDouble("amount");
                String username = resultSet.getString("username");
                String productName = resultSet.getString("productName");
                String text = String.format("+RM %.2f => %s purchased %s.", amount, username, productName);
                label.setText(text);
                labels[i++] = label;
            }
            return labels;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Button sellerButton(int userID) {
        User seller = new User(userID);
        Button button = new Button();
        button.setPrefWidth(175);
        button.setPrefHeight(100);
        button.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");
        button.setText(seller.getUsername());
        button.setWrapText(true);

        button.setOnAction(e -> {
            StoreController.setUser(seller);
            MainGUI.loadScene(Page.STORE);
        });

        return button;
    }

    public static Button categoryButton(Category category) {
        Button button = new Button();
        button.setPrefWidth(200);
        button.setPrefHeight(200);
        button.setStyle("""
                -fx-font-family: 'Montserrat';
                -fx-font-weight: bold;
                -fx-font-size: 14;""");
        button.setText(category.getDisplayName());
        button.setWrapText(true);

        return button;
    }

    private static void invokeAddReviewDialog(Order order, Product product, Label label) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add a review");
        dialog.setResizable(true);

        ButtonType ADD = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(ADD);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        GridPane gridPane = new GridPane();
        Text text = new Text("Enter your review: ");
        Text rating = new Text("Rating");
        Text subject = new Text("Subject");
        Text description = new Text("Description");
        TextField ratingField = new TextField();
        TextField subjectField = new TextField();
        TextArea descriptionField = new TextArea();
        Label message = new Label();

        gridPane.add(text, 0, 0);
        gridPane.add(rating, 0, 1);
        gridPane.add(subject, 0, 2);
        gridPane.add(description, 0, 3);
        for (int i = 1; i < 4; i++)
            gridPane.add(new Text(""), 1, i);
        gridPane.add(ratingField, 1, 1);
        gridPane.add(subjectField, 1, 2);
        gridPane.add(descriptionField, 1, 3);
        gridPane.add(message, 0, 4, 2, 1);
        gridPane.setMinWidth(500);
        gridPane.setMinHeight(300);

        gridPane.setHgap(5);
        gridPane.setVgap(20);
        GridPane.setHalignment(message, HPos.CENTER);
        dialog.getDialogPane().setContent(gridPane);

        final Button changeButton = (Button) dialog.getDialogPane().lookupButton(ADD);
        changeButton.addEventFilter(ActionEvent.ACTION, event -> {

            double ratingText = 0.0;
            try {
                ratingText = Double.parseDouble(ratingField.getText());
            } catch (NumberFormatException e) {
                message.setText("Please enter a valid number for rating.");
                event.consume();
            }

            if (ratingText < 1 || ratingText > 5) {
                message.setText("Rating should be between 1 and 5.");
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == ADD) {
                double ratingText = Double.parseDouble(ratingField.getText());
                Review review = new Review(Main.getUser().getUserID(), product.getProductID(), ratingText, subjectField.getText(), descriptionField.getText());
                order.setRated(true);
                Database.updateDatabase(review.insertQuery());
                Database.updateDatabase(order.updateQuery());
                label.setStyle("""
                        -fx-background-color: linear-gradient(to bottom, #ffffff, #f7f7f7, #eeeeee, #e6e6e6, #dedede);
                        -fx-border-color: black;
                        -fx-border-width: 1;
                        -fx-border-radius: 3;
                        """);
                System.out.println("Review added.");
            }
        });
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

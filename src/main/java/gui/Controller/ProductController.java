package main.java.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.java.*;
import main.java.gui.MainGUI;

public class ProductController {

    private static Product product;
    private static User user;
    private int productID, cartID, favouriteID;

    @FXML BorderPane borderPane;
    @FXML VBox vBox;
    @FXML private Button cartButton, buyNow, favouriteButton, editButton;
    @FXML private Label name, seller, rating, reviewCount, price, sales, stock,
            bestSelling, shippingFee, totalPrice;
    @FXML private Text description;
    @FXML private ImageView imageView;

    @FXML
    public void initialize() {

        user = Main.getUser();
        productID = product.getProductID();

        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        cartID = Cart.cartExists(user.getUserID(), productID);
        cartButton.setText(cartID == 0 ? "Add to Cart" : "Remove from Cart");
        favouriteID = Favourite.favouriteExists(user.getUserID(), productID);
        favouriteButton.setText(favouriteID == 0 ? "Add to Favourites" : "Remove from Favourites");

        cartButton.setOnAction(e -> cartButtonAction());
        favouriteButton.setOnAction(e -> favouriteButtonAction());
        editButton.setOnAction(e -> invokeEditProductDialog());
        setProductInformation(productID);

        StoredDB[] reviews = Review.getProductReviews(productID);
        if (reviews == null)
            return;
        for (StoredDB s : reviews) {
            Review r = (Review) s;
            vBox.getChildren().add(setLabel(r));
        }
    }

    private void cartButtonAction() {
        cartID = Cart.cartExists(user.getUserID(), productID);
        if (cartID == 0) {
            Main.getUser().addToCart(productID);
            cartButton.setText("Remove from Cart");
        } else {
            String query = "DELETE FROM Cart WHERE cartID = " + cartID;
            Database.updateDatabase(query);
            cartButton.setText("Add to Cart");
        }
    }

    private void favouriteButtonAction() {
        favouriteID = Favourite.favouriteExists(user.getUserID(), productID);
        if (favouriteID == 0) {
            Main.getUser().addToFavourites(productID);
            favouriteButton.setText("Remove from Favourites");
        } else {
            String query = "DELETE FROM Favourite WHERE favouriteID = " + favouriteID;
            Database.updateDatabase(query);
            favouriteButton.setText("Add to Favourites");
        }
    }

    private void setProductInformation(int productID) {
        setProductInformation(new Product(productID));
    }

    private void setProductInformation(Product product) {
        bestSelling.setText(product.isBestSelling() ? "Best Selling" : "");
        name.setText(product.getProductName());
        seller.setText("from " + "SELLER");
        rating.setText(String.format("Rating: %.2f (%d reviews)", 0.0, 0));
        price.setText(String.format("Price: RM %.2f", product.getPrice()));
        shippingFee.setText("Shipping Fee: RM " + "0.0");
        totalPrice.setText("Total Price: RM" + product.getPrice());
        sales.setText("Sales: " + product.getSales());
        stock.setText("Stock: " + product.getStock());

        description.setText(product.getDescription());
//        description.setMinHeight(70);
//        description.setOnMouseClicked(e -> {
//            if (description.getHeight() < 200)
//                description.setMinHeight(200);
//            else
//                description.setMinHeight(70);
//        });

        Image image = MainGUI.decode(product.getBase64String());
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setImage(image);

        name.setWrapText(true);
//        description.setWrapText(true);
    }

    public Label setLabel(Review review) {
        Label label = new Label();
        GridPane labelPane = new GridPane();
        label.setMinWidth(vBox.getWidth());
        label.setMinHeight(100);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setGraphic(labelPane);

        Label rating = new Label("" + review.getRating());
        Label subject = new Label(review.getSubject());
        Label description = new Label(review.getDescription());
//        Label datetime = new Label(review.getDatetime().toString());

        label.setStyle("""
                -fx-background-color: #FFF2E0;
                -fx-border-color: black;
                -fx-border-width: 1;
                -fx-border-radius: 3;""");
        label.setOnMouseClicked(e -> invokeAddCommentDialog());

        labelPane.add(rating, 1, 0);
        labelPane.add(subject, 1, 1);
        labelPane.add(description, 1, 2);
//        labelPane.add(datetime, 1, 3);

        return label;
    }

    private void invokeEditProductDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");

        ButtonType ADD = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(ADD);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        FXMLLoader dialogContentLoader = MainGUI.getLoaderWithNode("EditProductDialog");

        dialog.getDialogPane().setContent(dialogContentLoader.getRoot());
        dialog.setResizable(true);

        EditProductDialogController controller = dialogContentLoader.getController();
        controller.setProduct(product);
        controller.setText();

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
                product.setProductName(controller.getName());
                product.setCategory(Category.valueOf(controller.getSelectedCategory().toUpperCase().replace(" ", "_")));
                product.setDecsription(controller.getDescription());
                product.setPrice(Double.parseDouble(controller.getPrice()));
                product.setStock(Integer.parseInt(controller.getStock()));
                System.out.println(product);
                setProductInformation(product);
                // TODO change product to indatabase
                Database.updateDatabase(product.updateQuery());
                System.out.println("Done");
            }
        });
    }

    private void invokeAddCommentDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Comment");
        dialog.setResizable(true);

        ButtonType ADD = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(ADD);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        VBox box = new VBox();
        Label label = new Label("Add comment:");
        TextArea commentArea = new TextArea();
        box.getChildren().add(label);
        box.getChildren().add(commentArea);
        dialog.getDialogPane().setContent(box);



        final Button addButton = (Button) dialog.getDialogPane().lookupButton(ADD);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            String comment = commentArea.getText();
            if (comment.equals(""))
                event.consume();
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == ADD) {
                System.out.println("Done");
            }
        });
    }

    public static void setProduct(Product product) {
        ProductController.product = product;
    }
}

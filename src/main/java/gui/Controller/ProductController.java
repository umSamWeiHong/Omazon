package main.java.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.java.*;
import main.java.gui.MainGUI;
import main.java.gui.Page;

public class ProductController extends Controller {

    private static Product product;
    private static User user;
    private static boolean isSeller;
    private static int productID, sellerID, cartID, favouriteID;

    @FXML BorderPane borderPane;
    @FXML VBox vBox;
    @FXML private Button cartButton, favouriteButton, editButton, removeButton;
    @FXML private Label name, seller, rating, price, sales, stock,
            bestSelling;
    @FXML private Text description;
    @FXML private ImageView imageView;

    private static Label productNameLabel, productPriceLabel;

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        productID = product.getProductID();
        sellerID = product.getSellerID();

        cartID = Cart.cartExists(user.getUserID(), productID);
        cartButton.setText(cartID == 0 ? "Add to Cart" : "Remove from Cart");
        favouriteID = Favourite.favouriteExists(user.getUserID(), productID);
        favouriteButton.setText(favouriteID == 0 ? "Add to Favourites" : "Remove from Favourites");

        seller.setOnMouseClicked(e -> {
            StoreController.setSeller(new User(sellerID));
            MainGUI.loadScene(Page.STORE);
        });
        cartButton.setOnAction(e -> cartButtonAction());
        favouriteButton.setOnAction(e -> favouriteButtonAction());
        editButton.setOnAction(e -> invokeEditProductDialog());
        removeButton.setOnAction(e -> {
            Database.updateDatabase(product.deleteQuery());
            StoreController.setSeller(user);
            MainGUI.loadScene(Page.STORE);
        });
        setProductInformation(productID);

        System.out.println(user.getUserID() + " " + sellerID);
        isSeller = user.getUserID() == sellerID;
        cartButton.setDisable(isSeller);
        favouriteButton.setDisable(isSeller);
        editButton.setVisible(isSeller);
        removeButton.setVisible(isSeller);

        StoredDB[] reviews = Review.getProductReviews(productID);
        if (reviews == null)
            return;
        for (StoredDB s : reviews) {
            Review r = (Review) s;
            vBox.getChildren().add(setReviewLabel(r));
        }
    }

    private void cartButtonAction() {
        cartID = Cart.cartExists(user.getUserID(), productID);
        if (cartID == 0) {
            invokeAddCartDialog();
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
        seller.setText("from " + new User(sellerID).getUsername());
        rating.setText(String.format("Rating: %.2f", product.getRating()));
        price.setText(String.format("Price: RM %.2f", product.getPrice()));
        sales.setText("Sales: " + product.getSales());
        stock.setText("Stock: " + product.getStock());

        description.setText(product.getDescription());

        Image image = MainGUI.decode(product.getBase64String());
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        imageView.setImage(image);

        name.setWrapText(true);
    }

    public Label setReviewLabel(Review review) {
        Label label = new Label();
        GridPane labelPane = new GridPane();
        label.setMinWidth(vBox.getWidth());
        label.setMinHeight(100);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setGraphic(labelPane);

        Label rating = new Label("" + review.getRatingStars());
        Label subject = new Label(review.getSubject());
        Label description = new Label(review.getDescription());
        Label sellerComment = new Label();

        String commentText = review.getSellerComment();
        if (commentText != null && !commentText.equals(""))
            sellerComment.setText("Seller commented: " + commentText);

        rating.setStyle("-fx-text-fill: orange;");

        label.setStyle("""
                -fx-background-color: #FFF2E0;
                -fx-border-color: black;
                -fx-border-width: 1;
                -fx-border-radius: 3;""");
        // TODO Check if it is seller.
        label.setOnMouseClicked(e -> {
            if (!isSeller)
                return;
            String text = review.getSellerComment();
            if (text == null || text.equals(""))
                invokeAddCommentDialog(review, sellerComment);
        });

        labelPane.add(rating, 1, 0);
        labelPane.add(subject, 1, 1);
        labelPane.add(description, 1, 2);
        labelPane.add(sellerComment, 1, 3);

        return label;
    }

    private void invokeAddCartDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add to Cart");
        dialog.setResizable(true);

        ButtonType ADD = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(ADD);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        GridPane gridPane = new GridPane();
        Text text = new Text("Enter the quantity: ");
        TextField quantityField = new TextField("1");
        Label message = new Label();

        gridPane.add(text, 0, 0);
        gridPane.add(quantityField, 1, 0);
        gridPane.add(message, 0, 1, 2, 1);

        gridPane.setHgap(5);
        gridPane.setVgap(20);
        GridPane.setHalignment(message, HPos.CENTER);
        dialog.getDialogPane().setContent(gridPane);

        final Button changeButton = (Button) dialog.getDialogPane().lookupButton(ADD);
        changeButton.addEventFilter(ActionEvent.ACTION, event -> {

            int quantity = 0;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                message.setText("Please enter a valid number for quantity.");
                event.consume();
            }

            if (quantity < 1) {
                message.setText("Quantity cannot be less than 1.");
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == ADD) {
                int quantity = Integer.parseInt(quantityField.getText());
                Cart cart = new Cart(user.getUserID(), productID, quantity);
                Database.updateDatabase(cart.insertQuery());
                cartButton.setText("Remove from Cart");
                System.out.println("Done");
            }
        });
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
                String name = controller.getName();
                double price = Double.parseDouble(controller.getPrice());

                product.setProductName(name);
                product.setCategory(Category.valueOf(controller.getSelectedCategory().toUpperCase().replace(" ", "_")));
                product.setDecsription(controller.getDescription());
                product.setPrice(price);
                product.setStock(Integer.parseInt(controller.getStock()));

                productNameLabel.setText(name);
                productPriceLabel.setText(price + "");
                setProductInformation(product);
                Database.updateDatabase(product.updateQuery());
                System.out.println("Product edited.");
            }
        });
    }

    private void invokeAddCommentDialog(Review review, Label commentLabel) {

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
                String sellerComment = commentArea.getText();
                review.setComment(sellerComment);
                commentLabel.setText("Seller commented: " + sellerComment);
                Database.updateDatabase(review.updateQuery());
                System.out.println("Review updated.");
            }
        });
    }

    public static void setProduct(Product product) {
        ProductController.product = product;
        productID = product.getProductID();
    }

    public static void setProductNameLabel(Label productNameLabel) {
        ProductController.productNameLabel = productNameLabel;
    }

    public static void setProductPriceLabel(Label productPriceLabel) {
        ProductController.productPriceLabel = productPriceLabel;
    }
}

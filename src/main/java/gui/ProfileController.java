package main.java.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.java.Review;

import java.io.IOException;

public class ProfileController {

    @FXML VBox reviewBox;
    @FXML ScrollPane reviewScroll;
    @FXML GridPane gridPane;
    @FXML Button logout;

    @FXML
    public void initialize() {
//        Review review = new Review(3);
//        System.out.println(review);

//        for (Review review : Review.getUserReviews(1))
//            reviewBox.getChildren().add(setButton(review));
    }

    public Button setButton(Review review) {
        Button button = new Button();
        GridPane buttonPane = new GridPane();
        button.setMinWidth(reviewBox.getPrefWidth());
        button.setMinHeight(100);
        button.setGraphic(buttonPane);

        Label rating = new Label("" + review.getRating());
        Label subject = new Label(review.getSubject());
        Label description = new Label(review.getDescription());
        Label datetime = new Label(review.getDatetime().toString());

        buttonPane.add(rating, 1, 0);
        buttonPane.add(subject, 1, 1);
        buttonPane.add(description, 1, 2);
        buttonPane.add(datetime, 1, 3);

        return button;
    }
}

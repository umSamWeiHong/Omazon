package main.java.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LoginController {

    @FXML private StackPane stackPane;
    @FXML private ImageView logoView;
    @FXML private TextField username;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        Background background = new Background(new BackgroundFill(Colors.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY));
        stackPane.setBackground(background);
    }

    public void setText() {
        username.setText("Button clicked!");
    }
}

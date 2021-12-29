package main.java.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;

public class LoginController {

    @FXML private StackPane stackPane;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button button;

    @FXML
    public void initialize() {
        Background background = new Background(new BackgroundFill(Colors.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY));
        stackPane.setBackground(background);

        button.setOnAction(e -> printText());
    }

    public void printText() {
        System.out.println(username.getText());
        System.out.println(password.getText());
    }
}

package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import main.java.Login;
import main.java.Main;
import main.java.gui.Colors;
import main.java.gui.MainGUI;
import main.java.gui.Page;

import java.io.IOException;

public class LoginController extends Controller {

    @FXML private StackPane stackPane;
    @FXML private Rectangle rect;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button button;
    @FXML private Label message;
    @FXML private Hyperlink register;

    @FXML
    public void initialize() {
        Background background = new Background(new BackgroundFill(Colors.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY));
        stackPane.setBackground(background);

        DropShadow dropShadow2 = new DropShadow();
        dropShadow2.setOffsetX(6.0);
        dropShadow2.setOffsetY(4.0);

        rect.setEffect(dropShadow2);
        button.setOnAction(e -> login());
        register.setOnAction(e -> MainGUI.loadScene(Page.REGISTER));
    }

    @Override
    public void update() {

    }

    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        int userID = Login.validate(email, password);
        if (userID == 0)
            message.setText("The email or password is invalid.");
        else {
            message.setText("");
            Main.setUser(userID);
            MainGUI.loadScene(Page.HOME);
        }
    }
}

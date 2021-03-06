package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import main.java.Login;
import main.java.gui.Colors;
import main.java.gui.MainGUI;
import main.java.gui.Page;

public class RegisterController extends Controller {

    @FXML
    private StackPane stackPane;
    @FXML private Rectangle rect;
    @FXML private TextField usernameField, emailField;
    @FXML private PasswordField passwordField, confirmPasswordField, paymentPasswordField;
    @FXML private Button register, back;
    @FXML private Label message;

    @FXML
    public void initialize() {
        Background background = new Background(new BackgroundFill(Colors.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY));
        stackPane.setBackground(background);

        DropShadow dropShadow2 = new DropShadow();
        dropShadow2.setOffsetX(6.0);
        dropShadow2.setOffsetY(4.0);

        rect.setEffect(dropShadow2);

        register.setOnAction(e -> register());
        back.setOnAction(e -> MainGUI.loadScene(Page.LOGIN));
    }

    @Override
    public void update() {

    }

    public void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String paymentPassword = paymentPasswordField.getText();

        if (username.equals(""))
            message.setText("Username cannot be empty.");
        else if (email.equals(""))
            message.setText("Email cannot be empty.");
        else if (Login.emailExists(email))
            message.setText("This email already exists.");
        else if (password.equals(""))
            message.setText("Password cannot be empty");
        else if (paymentPassword.equals(""))
            message.setText("Payment password cannot be empty.");
        else if (!password.equals(confirmPassword))
            message.setText("The passwords are different.");
        else {
            Login.addNewUser(username, email, password, paymentPassword);
            MainGUI.loadScene(Page.LOGIN);
        }
    }
}

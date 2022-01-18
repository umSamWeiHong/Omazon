package main.java.gui.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.java.Database;
import main.java.Login;
import main.java.Main;
import main.java.User;
import main.java.gui.MainGUI;
import main.java.gui.Page;

public class SettingsController extends Controller {

    private static User user;

    @FXML private BorderPane borderPane;
    @FXML private TextField username, email;
    @FXML private TextArea shippingAddress;
    @FXML private Button passwordButton, paymentPasswordButton, update, delete;
    @FXML private Label message;

    // TODO add delete account

    private static Timeline timeline;

    @FXML
    public void initialize() {
        setButtonOnAction();
        setFadeTextTransition();
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();

        setInitialText();
    }

    private void setInitialText() {
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        shippingAddress.setText(user.getShippingAddress());
    }

    private void setButtonOnAction() {
        passwordButton.setOnAction(e -> invokeChangePasswordDialog());
        paymentPasswordButton.setOnAction(e -> invokeChangePaymentPasswordDialog());
        update.setOnAction(e -> updateInformation());
        delete.setOnAction(e -> deleteAccount());
    }

    private void updateInformation() {
        user.setUsername(username.getText());
        user.setEmail(email.getText());
        user.setShippingAddress(shippingAddress.getText());
        Database.updateDatabase(user.updateQuery());

        setMessage("Updated Successfully");
    }

    public void setFadeTextTransition() {
        KeyValue visibleValue = new KeyValue(message.opacityProperty(), 1);
        KeyValue invisibleValue = new KeyValue(message.opacityProperty(), 0);
        KeyFrame visibleFrame = new KeyFrame(Duration.seconds(2), visibleValue);
        KeyFrame invisibleFrame = new KeyFrame(Duration.seconds(2), invisibleValue);

        timeline = new Timeline(invisibleFrame, visibleFrame);
    }

    private void deleteAccount() {
        String query = "DELETE FROM User WHERE userID = " + user.getUserID();
        Database.updateDatabase(query);
        MainGUI.loadScene(Page.LOGIN);
    }

    private void invokeChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setResizable(true);

        ButtonType CHANGE = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(CHANGE);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        GridPane gridPane = new GridPane();
        Text oldPassword = new Text("Enter old password");
        Text newPassword = new Text("Enter new password");
        Text confirmPassword = new Text("Confirm new password");
        PasswordField oldPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        Label message = new Label();

        gridPane.add(oldPassword, 0, 0);
        gridPane.add(newPassword, 0, 1);
        gridPane.add(confirmPassword, 0, 2);
        for (int i = 0; i < 3; i++)
            gridPane.add(new Text(":"), 1, i);
        gridPane.add(oldPasswordField, 2, 0);
        gridPane.add(newPasswordField, 2, 1);
        gridPane.add(confirmPasswordField, 2, 2);
        gridPane.add(message, 0, 3, 3, 1);

        gridPane.setHgap(5);
        gridPane.setVgap(20);
        GridPane.setHalignment(message, HPos.CENTER);
        dialog.getDialogPane().setContent(gridPane);

        final Button changeButton = (Button) dialog.getDialogPane().lookupButton(CHANGE);
        changeButton.addEventFilter(ActionEvent.ACTION, event -> {

            String oldPasswordText = oldPasswordField.getText();
            String newPasswordText = newPasswordField.getText();
            String confirmPasswordText = confirmPasswordField.getText();

            if (!Login.validate(user.getUserID(), oldPasswordText)) {
                message.setText("Invalid old password.");
                event.consume();
            }
            if (newPasswordText.equals("")) {
                message.setText("Password cannot be empty");
                event.consume();
            }
            if (!newPasswordText.equals(confirmPasswordText)) {
                message.setText("The passwords are different.");
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == CHANGE) {
                user.setPassword(newPasswordField.getText());
                String query = String.format("""
                                        UPDATE `User`
                                        SET password = '%s'
                                        WHERE userID = %d""",
                                        newPasswordField.getText(), user.getUserID());
                Database.updateDatabase(query);
                setMessage("Password Updated Successfully");
                System.out.println("Done");
            }
        });
    }

    private void invokeChangePaymentPasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Payment Password");
        dialog.setResizable(true);

        ButtonType CHANGE = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        ButtonType CANCEL = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(CHANGE);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);

        GridPane gridPane = new GridPane();
        Text oldPassword = new Text("Enter old password");
        Text newPassword = new Text("Enter new password");
        Text confirmPassword = new Text("Confirm new password");
        PasswordField oldPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        Label message = new Label();

        gridPane.add(oldPassword, 0, 0);
        gridPane.add(newPassword, 0, 1);
        gridPane.add(confirmPassword, 0, 2);
        for (int i = 0; i < 3; i++)
            gridPane.add(new Text(":"), 1, i);
        gridPane.add(oldPasswordField, 2, 0);
        gridPane.add(newPasswordField, 2, 1);
        gridPane.add(confirmPasswordField, 2, 2);
        gridPane.add(message, 0, 3, 3, 1);

        gridPane.setHgap(5);
        gridPane.setVgap(20);
        GridPane.setHalignment(message, HPos.CENTER);
        dialog.getDialogPane().setContent(gridPane);

        final Button changeButton = (Button) dialog.getDialogPane().lookupButton(CHANGE);
        changeButton.addEventFilter(ActionEvent.ACTION, event -> {

            String oldPasswordText = oldPasswordField.getText();
            String newPasswordText = newPasswordField.getText();
            String confirmPasswordText = confirmPasswordField.getText();

            if (!oldPasswordText.equals(user.getPaymentPassword())) {
                message.setText("Invalid old password.");
                event.consume();
            }
            if (newPasswordText.equals("")) {
                message.setText("Password cannot be empty");
                event.consume();
            }
            if (!newPasswordText.equals(confirmPasswordText)) {
                message.setText("The passwords are different.");
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == CHANGE) {
                user.setPaymentPassword(newPasswordField.getText());
                String query = String.format("""
                                        UPDATE `User`
                                        SET paymentPassword = '%s'
                                        WHERE userID = %d""",
                                        newPasswordField.getText(), user.getUserID());
                Database.updateDatabase(query);
                setMessage("Payment Password Updated Successfully");
                System.out.println("Done");
            }
        });
    }

    private void setMessage(String message) {
        this.message.setOpacity(1);
        this.message.setText(message);
        timeline.play();
    }
}

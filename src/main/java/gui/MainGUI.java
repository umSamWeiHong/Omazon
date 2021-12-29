package main.java.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static void initStage(Stage stage) {
        stage.setTitle("Omazon");
        stage.getIcons().add(new Image("main/resources/img/icon.png"));
        stage.show();
    }

    public void loginScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        MainGUI.initStage(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        loginScene(stage);
    }
}

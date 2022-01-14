package main.java.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainGUI extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static void initStage() {
        stage.setTitle("Omazon");
        stage.getIcons().add(new Image("main/resources/img/icon.png"));
        stage.show();
    }

    public static void loadScene(Page page) {
        String filename = page.getFilename();
        try {
            Parent root = FXMLLoader.load(new File("src/main/resources/layout/" + filename + ".fxml").toURI().toURL());
            Scene scene = new Scene(root);
            // Add the respective css file if it exists.
            if (new File("src/main/resources/css/" + filename + ".css").exists())
                scene.getStylesheets().add(new File("src/main/resources/css/" + filename + ".css").toURI().toURL().toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void loadAllFonts() {
        File directory = new File("src/main/resources/font/");
        for (File file : directory.listFiles())
            Font.loadFont(file.toURI().toString(), 12);
    }

    @Override
    public void start(Stage stage) {
        MainGUI.stage = stage;
        MainGUI.loadAllFonts();
        MainGUI.initStage();
        MainGUI.loadScene(Page.LOGIN);
    }
}

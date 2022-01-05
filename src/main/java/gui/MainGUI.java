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

    private static Stage stage;

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
            Parent root = FXMLLoader.load(MainGUI.class.getResource(filename + ".fxml"));
            Scene scene = new Scene(root);
            // Add the respective css file if it exists.
            if (new File("src/main/java/gui/" + filename + ".css").exists())
                scene.getStylesheets().add(MainGUI.class.getResource(filename + ".css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void loadAllFonts() {
        File directory = new File("src/main/resources/fonts/");
        for (File file : directory.listFiles())
            Font.loadFont(MainGUI.class.getClassLoader().getResourceAsStream("main/resources/fonts/" + file.getName()), 12);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainGUI.stage = stage;
        MainGUI.loadAllFonts();
        MainGUI.initStage();
        MainGUI.loadScene(Page.PROFILE);
    }
}

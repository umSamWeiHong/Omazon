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
import java.net.MalformedURLException;
import java.net.URI;

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

    @SuppressWarnings("ConstantConditions")
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
        try {
            for (File file : directory.listFiles())
                Font.loadFont(file.toURL().toString(), 12);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        MainGUI.stage = stage;
        MainGUI.loadAllFonts();
        MainGUI.initStage();
//        MainGUI.loadScene(Page.PROFILE);
        ProductController.setProductID(3);
        MainGUI.loadScene(Page.PRODUCT);
    }
}

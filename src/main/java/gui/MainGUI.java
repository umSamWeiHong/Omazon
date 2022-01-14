package main.java.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.gui.Controller.ProductController;

import java.io.File;
import java.io.IOException;

public class MainGUI extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Parent getParentNode(String filename) {
        try {
            Parent root = FXMLLoader.load(new File("src/main/resources/layout/" + filename + ".fxml").toURI().toURL());
            // Add the respective css file if it exists.
            if (new File("src/main/resources/css/" + filename + ".css").exists())
                root.getStylesheets().add(new File("src/main/resources/css/" + filename + ".css").toURI().toString());
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initStage() {
        stage.setTitle("Omazon");
        stage.getIcons().add(new Image("main/resources/img/icon.png"));
        stage.show();
    }

    public static void loadScene(Page page) {
        double width = stage.getWidth();
        double height = stage.getHeight();

        @SuppressWarnings("ConstantConditions")
        Scene scene = new Scene(getParentNode(page.getFilename()));
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setScene(scene);
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
        ProductController.setProductID(5);
        MainGUI.loadScene(Page.PRODUCT);
    }
}

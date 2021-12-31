package main.java.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.Main;

import java.io.File;
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

    @SuppressWarnings("ConstantConditions")
    public static void loadAllFonts() {
        File directory = new File("src/main/resources/fonts/");
        for (File file : directory.listFiles())
            Font.loadFont(MainGUI.class.getClassLoader().getResourceAsStream("main/resources/fonts/" + file.getName()), 12);
    }

    @SuppressWarnings("ConstantConditions")
    public void loginScene(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene loginPage = new Scene(root);

        stage.setScene(loginPage);
    }

    @SuppressWarnings("ConstantConditions")
    public void profileScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        System.out.println();
        Image image = new Image("main/resources/img/icon.png");
        System.out.println(image.getHeight());
        String css = this.getClass().getResource("Profile.css").toExternalForm();
        Scene profilePage = new Scene(root);

        //Font.loadFont(MainGUI.class.getResourceAsStream("main/resources/fonts/Montserrat-Regular.otf"), 12);


        profilePage.getStylesheets().add(css);

        stage.setScene(profilePage);

        Button button = (Button) profilePage.lookup("#middleButton");
        button.setTextFill(Color.RED);

        stage.setScene(profilePage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainGUI.loadAllFonts();
        MainGUI.initStage(stage);
        profileScene(stage);
    }
}

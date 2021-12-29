package main.java.gui;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI {



    public static void initStage(Stage stage) {
        stage.setTitle("Omazon");
        stage.getIcons().add(new Image("main/resources/img/icon.png"));
        stage.show();
    }
}

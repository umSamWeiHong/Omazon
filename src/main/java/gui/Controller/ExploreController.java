package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.Category;
import main.java.Favourite;
import main.java.Main;
import main.java.User;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

import java.util.ArrayList;
import java.util.HashMap;

public class ExploreController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private FlowPane list;

    @FXML
    public void initialize() {
        for (Category category : Category.values()) {
            list.getChildren().add(DBNode.categoryButton(category));
        }
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());
    }
}

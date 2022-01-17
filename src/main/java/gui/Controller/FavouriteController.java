package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private FlowPane list;

    private static User user;

    private static HashMap<Integer, Button> map = new HashMap<>();

    @FXML
    public void initialize() {

    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        user.updateFavouriteIDs();
        int[] ids = user.getFavouriteIDs();
        updateMap(ids);
    }

    private void updateMap(int[] ids) {

        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i : map.keySet())
            if (!Main.linearSearch(ids, i))
                toRemove.add(i);

        for (int i : toRemove) {
            list.getChildren().remove(map.get(i));
            map.remove(i);
        }

        for (int id : ids)
            if (!map.containsKey(id)) {
                try {
                    Button button = DBNode.productButton(new Favourite(id).getProductID());
                    list.getChildren().add(button);
                    map.put(id, button);
                } catch (IllegalArgumentException e) {
                    System.out.println("FavouriteController: ProductID not found.");
                }
            }
    }
}

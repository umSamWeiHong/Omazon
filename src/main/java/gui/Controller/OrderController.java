package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.*;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private FlowPane list;

    private static User user;

    private static HashMap<Integer, Label> map = new HashMap<>();

    @FXML
    public void initialize() {
//        user = Main.getUser();
//
//        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
//        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());
//
//        StoredDB[] orders = Order.getUserOrders(user.getUserID());
//        if (orders == null)
//            return;
//        else {
//            for (StoredDB order : orders) {
//
//                Label label = DBNode.orderLabel((Order) order);
//
//                list.getChildren().add(label);
//            }
//        }
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());

        user = Main.getUser();
        user.updateOrderIDs();
        int[] ids = user.getOrderIDs();
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
                    Label label = DBNode.orderLabel(new Order(id).getOrderID());
                    list.getChildren().add(label);
                    map.put(id, label);
                } catch (IllegalArgumentException e) {
                    System.out.println("OrderController: ProductID not found.");
                }
            }
    }
}

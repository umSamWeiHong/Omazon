package main.java.gui.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import main.java.Database;
import main.java.gui.DBNode;
import main.java.gui.MainGUI;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchController extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private TextField searchStatement;
    @FXML private Button searchButton;
    @FXML private FlowPane list;

    private static String statement;

    @FXML
    public void initialize() {
        searchButton.setOnAction(e -> search());
    }

    @Override
    public void update() {
        borderPane.setTop(MainGUI.getMenuBarLoader().getRoot());
        borderPane.setLeft(MainGUI.getSlideMenuLoader().getRoot());
        searchStatement.setText(statement);
        search();
    }

    public static void setSearchStatement(String searchStatement) {
        statement = searchStatement;
    }

    private void search() {
        list.getChildren().clear();
        searchProduct();
        searchSeller();
    }

    private void searchProduct() {
        String statement = searchStatement.getText();
        String query = String.format("""
                SELECT productID FROM Product WHERE productName LIKE '%%%s%%' LIMIT 10
                """, statement);
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            return;

        try {
            while (resultSet.next()) {
                int productID = resultSet.getInt("productID");
                list.getChildren().add(DBNode.productButton(productID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchSeller() {
        String statement = searchStatement.getText();
        String query = String.format("""
                SELECT userID FROM User WHERE username LIKE '%%%s%%' LIMIT 10
                """, statement);
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            return;

        try {
            while (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                list.getChildren().add(DBNode.sellerButton(userID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

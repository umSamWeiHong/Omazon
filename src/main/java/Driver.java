package main.java;

import java.sql.*;

public class Driver {

    public static void main(String[] args) {

        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery("select * from omazon.Product");

            while (result.next())
                System.out.println(result.getString(2));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://139.99.88.50:3306/omazon";
        String username = "omazonremote";
        String password = "f798SuFM";

        return DriverManager.getConnection(url, username, password);
    }
}

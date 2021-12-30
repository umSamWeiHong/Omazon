package main.java;

import java.sql.*;

public class Driver {

    public static void main(String[] args) {

        try {
            ResultSet result = queryDatabase("select * from Product");

            while (result.next())
                System.out.println(result.getString(2));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet queryDatabase(String query) throws SQLException {
        String url = "jdbc:mysql://139.99.88.50:3306/omazon";
        String user = "omazonremote";
        String password = "f798SuFM";

        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
}

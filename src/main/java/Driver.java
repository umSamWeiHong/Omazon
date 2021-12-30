package main.java;

import java.sql.*;

public class Driver {

    private static String url = "jdbc:mysql://139.99.88.50:3306/omazon",
                          user = "omazonremote",
                          password = "f798SuFM";
    private static Connection connection = null;

    static {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static void updateDatabase(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
}

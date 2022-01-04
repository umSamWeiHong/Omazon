package main.java;

import java.sql.*;

public class Database {

    private static final String url = "jdbc:mysql://139.99.88.50:3306/omazon",
                                user = "omazonremote",
                                password = "f798SuFM";
    private static Connection connection = null;

    // TODO Retry Connection if failed
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
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(query);
    }

//    @Deprecated
//    public static void updateDatabase(String query) throws SQLException {
//        Statement statement = connection.createStatement();
//        statement.executeUpdate(query);
//    }

    /** Return true if the database is updated, false otherwise. */
    private static boolean updateDatabase(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /** Add this object to database. */
    public static boolean add(StoredDB dbObject) {
        // Return false if the object is already in database.
        if (dbObject.inDatabase())
            return false;
        if (updateDatabase(dbObject.insertQuery()))
            dbObject.setInDatabase(true);
        return dbObject.inDatabase();
    }

    /** Update this object in database. */
    public static boolean update(StoredDB dbObject) {
        // Return false if the object is not in database.
        if (!dbObject.inDatabase())
            return false;
        return updateDatabase(dbObject.updateQuery());
    }

    /** Delete this object in database. */
    public static boolean delete(StoredDB dbObject) {
        // Return false if the object is not in database.
        if (!dbObject.inDatabase())
            return false;
        if (updateDatabase(dbObject.deleteQuery()))
            dbObject.setInDatabase(false);
        return !dbObject.inDatabase();
    }
}

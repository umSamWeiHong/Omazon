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

    /** Query the database and return ResultSet if found, null otherwise. */
    public static ResultSet queryDatabase(String query) {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.isBeforeFirst())
                return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Return all IDs of objects that satisfies the query */
    public static int[] getID(String query, String label) {
        return getID(query, label, -1);
    }

    /** Return the N IDs of recent objects that satisfies the query */
    public static int[] getID(String query, String label, int N) {
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            return null;

        // If N is -1, initialize the array with length as number of results.
        if (N == -1) {
            try {
                resultSet.last();
                N = resultSet.getRow();
                resultSet.beforeFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        int[] ID = new int[N];
        try {
            int count = 0;
            while (resultSet.next())
                ID[count++] = resultSet.getInt(label);
            return ID;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Return true if the database is updated, false otherwise. */
    // TODO Change to private after all objects are updated.
    public static boolean updateDatabase(String query) {
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

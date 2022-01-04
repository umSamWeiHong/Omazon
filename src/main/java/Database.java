package main.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

    /** Return the N recent objects whose ID satisfies the query.
     *  To get all objects, set N = -1.
     */
    public static StoredDB[] getID(String query, Class<? extends StoredDB> objectClass, int N) {
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            return null;

        // If N is -1, set the array length, N as number of results.
        if (N == -1) {
            try {
                resultSet.last();
                N = resultSet.getRow();
                resultSet.beforeFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        StoredDB[] dbObjects = new StoredDB[N];

        try {
            int count = 0;
            Method m = objectClass.getMethod("getPrimaryKey");
            String primaryKey = (String) m.invoke(null);
            Constructor<? extends StoredDB> constructor = objectClass.getConstructor(int.class);
            while (resultSet.next()) {
                int ID = resultSet.getInt(primaryKey);
                dbObjects[count++] = constructor.newInstance(ID);
            }
            return dbObjects;
        } catch (Exception e) {
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

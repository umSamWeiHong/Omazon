package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class Favourite extends StoredDB{

    private int favouriteID, userID, productID;
    private Timestamp dateAdded;

    /** Create a Favourite object with data from database. */
    public Favourite(int favouriteID) {
        String query = "SELECT * FROM Favourite WHERE favouriteID = " + favouriteID;
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            throw new IllegalArgumentException("FavouriteID is not found.");

        try {
            resultSet.next();
            this.favouriteID = resultSet.getInt("favouriteID");
            userID = resultSet.getInt("userID");
            productID = resultSet.getInt("productID");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Favourite object with all parameters (except reviewID). */
    public Favourite(int userID, int productID) {
        this(userID, productID, null);
    }

    /** Create a new Favourite object with all parameters (except reviewID). */
    public Favourite(int userID, int productID, Timestamp dateAdded) {
        this.userID = userID;
        this.productID = productID;
        this.dateAdded = dateAdded;
    }

    public int getFavouriteID() {
        return favouriteID;
    }

    public int getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    /** Return the N recent favourites from the user. */
    public static StoredDB[] getUserFavourites(int userID, int N) {
        String query = String.format("SELECT favouriteID FROM Favourite " +
                        "WHERE userID = %d " +
                        "ORDER BY dateAdded DESC",
                        userID);

        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Favourite.class, N);
    }

    /** Return all favourites from the user. */
    public static StoredDB[] getUserFavourites(int userID) {
        return getUserFavourites(userID, -1);
    }

    @Override
    public String toString() {
        return "Favourite{" +
                "favouriteID=" + favouriteID +
                ", userID=" + userID +
                ", productID=" + productID +
                ", dateAdded=" + dateAdded +
                ", inDatabase=" + inDatabase() +
                '}';
    }

    public static int favouriteExists(int userID, int productID) {
        String query = String.format("""
                                SELECT favouriteID FROM Favourite
                                WHERE userID = %d AND productID = %d""",
                                userID, productID);

        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet != null)
            try {
                resultSet.next();
                return resultSet.getInt("favouriteID");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return 0;
    }

    public static String getPrimaryKey() {
        return "favouriteID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                "Favourite (userID, productID) " +
                "VALUES (%d, %d)",
                userID, productID);
    }

    @Override
    public String updateQuery() {
        return null;
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Favourite " +
               "WHERE favouriteID = " + favouriteID;
    }
}

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
        ResultSet resultSet = null;

        try {
            resultSet = Database.queryDatabase(query);
            // Throw exception when reviewID is not found.
            if (!resultSet.isBeforeFirst())
                throw new IllegalArgumentException("FavouriteID is not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet.next();
            this.favouriteID = resultSet.getInt("favouriteID");
            userID = resultSet.getInt("userID");
            productID = resultSet.getInt("productID");
            inDatabase = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Favourite object with all parameters (except reviewID). */
    public Favourite(int userID, int productID, Timestamp dateAdded) {
        this.userID = userID;
        this.productID = productID;
        this.dateAdded = dateAdded;
        inDatabase = false;
    }

    /** Return the N recent favourites from the user. */
    public static Favourite[] getUserFavourites(int userID, int N) {
        String query = String.format("SELECT favouriteID FROM Favourite " +
                        "WHERE userID = %d " +
                        "ORDER BY dateAdded DESC",
                        userID);

        if (N != -1) {
            query += " LIMIT " + N;
        }

        ResultSet resultSet = null;
        try {
            resultSet = Database.queryDatabase(query);
            if (!resultSet.isBeforeFirst())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Favourite[] favourites = new Favourite[N];
        try {
            int favouriteCount = 0;
            while (resultSet.next()) {
                favourites[favouriteCount] = new Favourite(resultSet.getInt("favouriteID"));
                favouriteCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favourites;
    }

    /** Return all favourites from the user. */
    public static Favourite[] getUserFavourites(int userID) {
        return getUserFavourites(userID, -1);
    }

    @Override
    public String toString() {
        return "Favourite{" +
                "favouriteID=" + favouriteID +
                ", userID=" + userID +
                ", productID=" + productID +
                ", dateAdded=" + dateAdded +
                ", inDatabase=" + inDatabase +
                '}';
    }

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            Favourite favourite1 = new Favourite(1, new Random().nextInt(10), new Timestamp(Instant.now().toEpochMilli()));
            Database.add(favourite1);
        }

        for (Favourite f : Favourite.getUserFavourites(1))
            System.out.println(f);
    }


    public static String getPrimaryKey() {
        return "";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                "Favourite (userID, productID, dateAdded) " +
                "VALUES (%d, %d, '%s')",
                userID, productID, dateAdded);
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

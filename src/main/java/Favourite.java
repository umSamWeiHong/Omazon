package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class Favourite {

    private int favouriteID, userID, productID;
    private Timestamp dateAdded;
    private boolean inDatabase;

    /** Create a Favourite object with data from database. */
    public Favourite(int favouriteID) {
        String query = "SELECT * FROM Favourite WHERE favouriteID = " + favouriteID;
        ResultSet resultSet = null;

        try {
            resultSet = Driver.queryDatabase(query);
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

    /** Add this Favourite object to database. */
    public void addToDatabase() {

        // Do nothing if the favourite is already in database.
        if (inDatabase) return;

        String insertQuery = String.format("INSERT INTO " +
                        "Favourite (userID, productID, dateAdded) " +
                        "VALUES (%d, %d, '%s')",
                userID, productID, dateAdded);
        Driver.updateDatabase(insertQuery);
        inDatabase = true;
    }

    /** Delete this Favourite object in database. */
    public void deleteFromDatabase() {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String deleteQuery = "DELETE FROM Favourite " +
                "WHERE favouriteID = " + favouriteID;
        Driver.updateDatabase(deleteQuery);
    }

    /** Return the N recent favourites from the user. */
    public static Favourite[] getUserFavourites(int userID, int N) {
        String query = String.format("SELECT favouriteID FROM Favourite " +
                        "WHERE userID = %d " +
                        "ORDER BY dateAdded DESC " +
                        "LIMIT %d",
                userID, N);

        ResultSet resultSet = null;
        try {
            resultSet = Driver.queryDatabase(query);
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
        String query = String.format("SELECT favouriteID FROM Favourite " +
                        "WHERE userID = %d " +
                        "ORDER BY dateAdded DESC ",
                userID);

        ResultSet resultSet = null;
        try {
            resultSet = Driver.queryDatabase(query);
            if (!resultSet.isBeforeFirst())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Initialize the array with number of results.
            resultSet.last();
            Favourite[] favourites = new Favourite[resultSet.getRow()];
            resultSet.beforeFirst();

            int favouriteCount = 0;
            while (resultSet.next()) {
                favourites[favouriteCount] = new Favourite(resultSet.getInt("favouriteID"));
                favouriteCount++;
            }
            return favourites;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            favourite1.addToDatabase();
        }

        for (Favourite f : Favourite.getUserFavourites(1))
            System.out.println(f);
    }
}

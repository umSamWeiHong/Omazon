package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class Review {

    private int reviewID, userID, productID;

    private Timestamp datetime;
    private double rating;

    private String subject, description, sellerComment;
    private boolean inDatabase;

    // Create a Review object with data from database.
    public Review(int reviewID) {
        String query = "SELECT * FROM Review WHERE reviewID = " + reviewID;
        ResultSet resultSet = null;

        try {
            resultSet = Driver.queryDatabase(query);
            // Throw exception when reviewID is not found.
            if (!resultSet.isBeforeFirst())
                throw new IllegalArgumentException("ReviewID is not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet.next();
            this.reviewID = resultSet.getInt("reviewID");
            userID = resultSet.getInt("userID");
            productID = resultSet.getInt("productID");
            datetime = resultSet.getTimestamp("datetime");
            rating = resultSet.getDouble("rating");
            subject = resultSet.getString("subject");
            description = resultSet.getString("description");
            sellerComment = resultSet.getString("sellerComment");
            inDatabase = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create a new Review object with all parameters (except reviewID and sellerComment).
    public Review(int userID, int productID, Timestamp datetime, double rating, String subject, String description) {
        this.userID = userID;
        this.productID = productID;
        this.datetime = datetime;
        this.rating = rating;
        this.subject = subject;
        this.description = description;
        inDatabase = false;
    }

    public void addToDatabase() throws SQLException {

        // Do nothing if the review is already in database.
        if (inDatabase) return;

        String insertQuery = String.format("INSERT INTO " +
                        "Review (userID, productID, datetime, rating, subject, description, sellerComment) " +
                        "VALUES (%d, %d, '%s', %f, '%s', '%s', '%s')",
                        userID, productID, datetime, rating, subject, description, sellerComment);
        Driver.updateDatabase(insertQuery);
        inDatabase = true;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", userID=" + userID +
                ", productID=" + productID +
                ", datetime=" + datetime +
                ", rating=" + rating +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", sellerComment='" + sellerComment + '\'' +
                ", inDatabase=" + inDatabase +
                '}';
    }

    public static void main(String[] args) {
        Timestamp sqlTime = new Timestamp(Instant.now().toEpochMilli());

        Review review = new Review(1, 3, sqlTime, 5.0, "Yahoo", "Google");
        try {
            review.addToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(review.datetime);

        Review review1 = new Review(8);
        System.out.println(review1);
    }
}

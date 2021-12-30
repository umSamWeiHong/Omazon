package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class Review {

    private int reviewID, userID, productID;

    private Timestamp datetime, commentDatetime;
    private double rating;

    private String subject, description, sellerComment;
    private boolean inDatabase;

    /** Create a Review object with data from database. */
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

    /** Create a new Review object with all parameters (except reviewID and sellerComment). */
    public Review(int userID, int productID, Timestamp datetime, double rating, String subject, String description) {
        this.userID = userID;
        this.productID = productID;
        this.datetime = datetime;
        this.rating = rating;
        this.subject = subject;
        this.description = description;
        inDatabase = false;
    }

    public int getReviewID() {
        return reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public Timestamp getCommentDatetime() {
        return commentDatetime;
    }

    public double getRating() {
        return rating;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getSellerComment() {
        return sellerComment;
    }

    /** For seller to comment */
    public void setComment(String comment, Timestamp datetime) {
        sellerComment = comment;
        commentDatetime = datetime;
    }

    /** Add this Review object to database. */
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

    public void updateDatabase() throws SQLException {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String updateQuery = String.format("UPDATE Review " +
                        "SET datetime = '%s', rating = %f, subject = '%s', description = '%s', sellerComment = '%s' " +
                        "WHERE reviewID = %d",
                        datetime, rating, subject, description, sellerComment, reviewID);
        Driver.updateDatabase(updateQuery);
    }

    public void deleteFromDatabase() throws SQLException {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String deleteQuery = "DELETE FROM Review" +
                             "WHERE reviewID = " + reviewID;
        Driver.updateDatabase(deleteQuery);
    }

    /** Return the N recent reviews from the user. */
    public static Review[] getUserReviews(int userID, int N) {
        String query = String.format("SELECT reviewID FROM Review " +
                            "WHERE userID = %d " +
                            "ORDER BY datetime DESC " +
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

        Review[] reviews = new Review[N];
        try {
            int reviewCount = 0;
            while (resultSet.next()) {
                reviews[reviewCount] = new Review(resultSet.getInt("reviewID"));
                reviewCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    /** Return all reviews from the user. */
    public static Review[] getUserReviews(int userID) {
        String query = String.format("SELECT reviewID FROM Review " +
                        "WHERE userID = %d " +
                        "ORDER BY datetime DESC ",
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
            Review[] reviews = new Review[resultSet.getRow()];
            resultSet.beforeFirst();

            int reviewCount = 0;
            while (resultSet.next()) {
                reviews[reviewCount] = new Review(resultSet.getInt("reviewID"));
                reviewCount++;
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
//
//        Review review = new Review(1, 3, sqlTime, 5.0, "Yahoo", "Google");
//        try {
//            review.addToDatabase();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(review.datetime);
//
        Review review1 = new Review(5);
        System.out.println(review1);
//        for (Review r : Review.getUserReviews(1))
//            System.out.println(r);

        review1.setComment("Very good!", sqlTime);
        try {
            review1.updateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(review1);
    }
}

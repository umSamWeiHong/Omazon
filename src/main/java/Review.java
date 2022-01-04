package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class Review extends StoredDB {

    private int reviewID, userID, productID;

    private Timestamp datetime, commentDatetime;
    private double rating;

    private String subject, description, sellerComment;

    /** Create a Review object with data from database. */
    public Review(int reviewID) {
        String query = "SELECT * FROM Review WHERE reviewID = " + reviewID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when reviewID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("ReviewID is not found.");

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
        if (description == null) return null;
        if (description.equals("null")) return "";
        return description;
    }

    public String getSellerComment() {
        return sellerComment;
    }

    /** Edit the review. */
    public void editReview(Timestamp datetime, double rating, String subject, String description) {
        this.datetime = datetime;
        this.rating = rating;
        this.subject = subject;
        this.description = description;
    }

    /** For seller to comment */
    public void setComment(String comment, Timestamp datetime) {
        sellerComment = comment;
        commentDatetime = datetime;
    }

    /** Return the N recent reviews from the user. */
    public static Review[] getUserReviews(int userID, int N) {
        String query = String.format("SELECT reviewID FROM Review " +
                            "WHERE userID = %d " +
                            "ORDER BY datetime DESC",
                            userID);
        if (N != -1)
            query += " LIMIT " + N;

        int[] IDs = Database.getID(query, "reviewID", N);

        if (N == -1)
            N = IDs.length;

        Review[] reviews = new Review[N];
        for (int i = 0; i < N; i++)
            reviews[i] = new Review(IDs[i]);
        return reviews;
    }

    /** Return all reviews from the user. */
    public static Review[] getUserReviews(int userID) {
        return getUserReviews(userID, -1);
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
//        Timestamp sqlTime =
        Review review1 = new Review(7);
        review1.userID = 2;
        review1.datetime = new Timestamp(Instant.now().toEpochMilli());
        review1.rating = 4;
        review1.inDatabase = false;

        Database.add(review1);
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                "Review (userID, productID, datetime, rating, subject, description, sellerComment) " +
                "VALUES (%d, %d, '%s', %f, '%s', '%s', '%s')",
                userID, productID, datetime, rating, subject, description, sellerComment);
    }

    @Override
    public String updateQuery() {
        return String.format("UPDATE Review " +
                "SET datetime = '%s', rating = %f, subject = '%s', description = '%s', sellerComment = '%s' " +
                "WHERE reviewID = %d",
                datetime, rating, subject, description, sellerComment, reviewID);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Review " +
               "WHERE reviewID = " + reviewID;
    }
}

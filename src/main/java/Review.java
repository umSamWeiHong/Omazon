package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
    public static StoredDB[] getUserReviews(int userID, int N) {
        String query = String.format("SELECT reviewID FROM Review " +
                            "WHERE userID = %d " +
                            "ORDER BY datetime DESC",
                            userID);
        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Review.class, N);
    }

    /** Return all reviews from the user. */
    public static StoredDB[] getUserReviews(int userID) {
        return getUserReviews(userID, -1);
    }

    /** Return the N recent reviews of the product. */
    public static StoredDB[] getProductReviews(int productID, int N) {
        String query = String.format("SELECT reviewID FROM Review " +
                        "WHERE productID = %d " +
                        "ORDER BY datetime DESC",
                        productID);
        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Review.class, N);
    }

    /** Return all reviews of the product. */
    public static StoredDB[] getProductReviews(int productID) {
        return getProductReviews(productID, -1);
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
//        StoredDB[] reviews = Database.generateObjects(Review.class, new int[]{9,10,11});
        StoredDB[] reviews = getUserReviews(3);
        for (StoredDB i : getProductReviews(3))
            System.out.println(i);
//        review1.userID = 2;
//        review1.datetime = new Timestamp(Instant.now().toEpochMilli());
//        review1.rating = 4;
//        review1.inDatabase = false;
//
//        Database.add(review1);
    }

    public static String getPrimaryKey() {
        return "reviewID";
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

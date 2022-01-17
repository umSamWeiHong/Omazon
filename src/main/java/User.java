package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class User extends StoredDB {

    private int userID;
    private String username;
    private String email;
    private String password;

    private int[] cartIDs, orderIDs, favouriteIDs;

    // Customer aspect
    private double balance;
    private Product[] cartProduct;
    private String[] orderHistory;
    private String paymentPassword, shippingAddress;

    // Seller
    private double profit;
    private Product[] productsList;
    private String[] transactionsHistory;
    private String[] orderNotifications;

    private Timestamp dateCreated;

    /** Create a User object with data from database. */
    public User(int userID) {
        String query = "SELECT * FROM User WHERE userID = " + userID;
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            throw new IllegalArgumentException("userID is not found.");

        try {
            resultSet.next();
            this.userID = resultSet.getInt("userID");
            username = resultSet.getString("username");
            email = resultSet.getString("email");
            password = resultSet.getString("password");
            balance = resultSet.getDouble("balance");
            profit = resultSet.getDouble("profit");
            paymentPassword = resultSet.getString("paymentPassword");
            shippingAddress = resultSet.getString("shippingAddress");
            dateCreated = resultSet.getTimestamp("dateCreated");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User(String username, String email, String password) {
        this(username, email, password, null);
    }

    /** Create a new User object with all parameters (except userID). */
    public User(String username, String email, String password, Timestamp dateCreated) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateCreated = dateCreated;
    }

    public void updateCartIDs() {
        String query = String.format("""
                                SELECT cartID FROM Cart
                                 WHERE userID = %d
                                 ORDER BY dateAdded DESC""", userID);
        cartIDs = Database.getIDs(query, "cartID");
    }

    public void updateOrderIDs() {
        String query = String.format("""
                                SELECT orderID FROM `Order`
                                 WHERE userID = %d
                                 ORDER BY orderTime DESC""", userID);
        orderIDs = Database.getIDs(query, "orderID");
    }

    public void updateFavouriteIDs() {
        String query = String.format("""
                                SELECT favouriteID FROM Favourite
                                 WHERE userID = %d
                                 ORDER BY dateAdded DESC""", userID);
        favouriteIDs = Database.getIDs(query, "favouriteID");
    }

    /** Edit the user details. */
    public void editDetails(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "user{" +
                "userID =" + userID +
                ", username =" + username +
                ", password =" + password +
                ", email =" + email +
                ", create_time =" + dateCreated +
                ", inDatabase=" + inDatabase() +
                '}';
    }

    public void setBalance(double amount) {
        balance = amount;
        Database.update(this);
    }

    public void addToCart(int productID) {
        Database.add(new Cart(userID, productID));
    }

    public void addToFavourites(int productID) {
        Database.add(new Favourite(userID, productID));
    }

    public static String getPrimaryKey() {
        return "userID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                        "`User` (username, email, password) " +
                        "VALUES ('%s', '%s', '%s')",
                        username, email, password);
    }

    @Override
    public String updateQuery() {
        return String.format("""
                        UPDATE `User`
                         SET username = '%s', email = '%s', password = '%s', balance = %.2f, shippingAddress = '%s',
                         paymentPassword = '%s'
                         WHERE userID = %d""",
                        username, email, password, balance, shippingAddress, paymentPassword, userID);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM `User` " +
               "WHERE userID = " + userID;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public Product[] getCartProduct() {
        return cartProduct;
    }

    public String[] getOrderHistory() {
        return orderHistory;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public double getProfit() {
        return profit;
    }

    public Product[] getProductsList() {
        return productsList;
    }

    public String[] getTransactionsHistory() {
        return transactionsHistory;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String[] getOrderNotifications() {
        return orderNotifications;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public int[] getCartIDs() {
        return cartIDs;
    }

    public int[] getOrderIDs() {
        return orderIDs;
    }

    public int[] getFavouriteIDs() {
        return favouriteIDs;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }
}
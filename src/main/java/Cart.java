/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author User
 */
public class Cart extends StoredDB{
    
    private int cartID, userID, productID;
    private Timestamp dateAdded;

    /** Create a Cart object with data from database. */
    public Cart(int cartID) {
        String query = "SELECT * FROM Cart WHERE cartID = " + cartID;
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            throw new IllegalArgumentException("CartID is not found.");

        try {
            resultSet.next();
            this.cartID = resultSet.getInt("cartID");
            userID = resultSet.getInt("userID");
            productID = resultSet.getInt("productID");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Cart object with all parameters (except reviewID). */
    public Cart(int userID, int productID, Timestamp dateAdded) {
        this.userID = userID;
        this.productID = productID;
        this.dateAdded = dateAdded;
    }

    /** Return the N recent cart items from the user. */
    public static StoredDB[] getCartItems(int userID, int N) {
        String query = String.format("SELECT cartID FROM Cart " +
                        "WHERE userID = %d " +
                        "ORDER BY dateAdded DESC",
                        userID);

        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Cart.class, N);
    }

    /** Return all cart items from the user. */
    public static StoredDB[] getCartItems(int userID) {
        return getCartItems(userID, -1);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartID=" + cartID +
                ", userID=" + userID +
                ", productID=" + productID +
                ", dateAdded=" + dateAdded +
                ", inDatabase=" + inDatabase() +
                '}';
    }

    public static void main(String[] args) {

//        for (int i = 0; i < 3; i++) {
//            Favourite favourite1 = new Favourite(2, new Random().nextInt(10), new Timestamp(Instant.now().toEpochMilli()));
//            Database.add(favourite1);
//        }

        Cart toDelete = new Cart(60);
        System.out.println(toDelete);
        Database.delete(toDelete);

        for (StoredDB cr : Cart.getCartItems(5)) {
            System.out.println(cr);
        }
    }

    public static String getPrimaryKey() {
        return "cartID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                "Cart (userID, productID, dateAdded) " +
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
               "WHERE cartID = " + cartID;
    }
}
    


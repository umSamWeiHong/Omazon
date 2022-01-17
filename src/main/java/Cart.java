package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Cart extends StoredDB{

    private int cartID, userID, productID, quantity;
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
            quantity = resultSet.getInt("quantity");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cart(int userID, int productID) {
        this(userID, productID, 1);
    }

    /** Create a new Cart object with all parameters (except reviewID). */
    public Cart(int userID, int productID, int quantity) {
        this.userID = userID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public int getCartID() {
        return cartID;
    }

    public int getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
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

    public static int cartExists(int userID, int productID) {
        // TODO static
        String query = String.format("""
                                SELECT cartID FROM Cart
                                WHERE userID = %d AND productID = %d""",
                                userID, productID);

        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet != null)
            try {
                resultSet.next();
                return resultSet.getInt("cartID");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return 0;
    }

    /** Return all cart items from the user. */
    public static StoredDB[] getCartItems(int userID) {
        return getCartItems(userID, -1);
    }

    public static double getTotalAmount(StoredDB[] cartItems) {
        double amount = 0;
        for (StoredDB item : cartItems) {
            Cart cart = (Cart) item;
            amount += cart.getQuantity() * new Product(cart.getProductID()).getPrice();
        }
        return amount;
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
                "Cart (userID, productID, quantity) " +
                "VALUES (%d, %d, %d)",
                userID, productID, quantity);
    }

    @Override
    public String updateQuery() {
        return null;
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Cart " +
               "WHERE cartID = " + cartID;
    }
}
    


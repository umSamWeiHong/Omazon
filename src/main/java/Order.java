package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order extends StoredDB {

    private int orderID, productID, userID, sellerID, orderQuantity;
    private String shippingAddress;

    public Order() {
        orderID = 0;
        productID = 0;
        userID = 0;
        sellerID = 0;
        shippingAddress = "";
        orderQuantity = 0;
    }

    /** Create an Order object with data from database. */
    public Order(int orderID) {
        String query = "SELECT * FROM `Order` WHERE orderID = " + orderID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when orderID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("OrderID is not found.");

        try {
            resultSet.next();
            this.orderID = resultSet.getInt("orderID");
            productID = resultSet.getInt("productID");
            userID = resultSet.getInt("userID");
            sellerID = resultSet.getInt("sellerID");
            shippingAddress = resultSet.getString("shippingAddress");
            orderQuantity = resultSet.getInt("orderQuantity");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Order object with all parameters */
    public Order(int orderID, int productID, int userID, int sellerID, String shippingAddress, int orderQuantity) {
        this.orderID = orderID;
        this.productID = productID;
        this.userID = userID;
        this.sellerID = sellerID;
        this.shippingAddress = shippingAddress;
        this.orderQuantity = orderQuantity;
    }

    // Accesor
    public int getOrderID() { return orderID; }
    public int getProductID() { return productID; }
    public int getUserID() { return userID; }
    public int getSellerID() { return sellerID; }
    public String getShippingAddress() { return shippingAddress; }
    public int getOrderQuantity() { return orderQuantity; }
    // Mutator
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public void setProductID(int productID) { this.productID = productID; }
    public void setUserID(int userID) { this.userID = userID; }
    public void setSellerID(int sellerID) { this.sellerID = sellerID; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setOrderQuantity(int orderQuantity) { this.orderQuantity = orderQuantity; }

    /** Return the N recent orders from the user. */
    public static StoredDB[] getUserOrders(int userID, int N) {
        String query = String.format("SELECT orderID FROM `Order` " +
                        "WHERE userID = %d " +
                        "ORDER BY orderTime DESC",
                userID);

        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Order.class, N);
    }

    /** Return all orders from the user. */
    public static StoredDB[] getUserOrders(int userID) {
        return getUserOrders(userID, -1);
    }

    /** Return the order quantity for specific product from the user. */
    public static StoredDB[] getUserOrderQuantity(int userID, int productID) {
        String query = String.format("SELECT orderQuantity FROM `Order` " +
                        "WHERE userID = %d AND productID = %d ",
                userID, productID);

        return Database.getDBObjects(query, Order.class, -1);
    }

    /** Return the order quantity from the user. */
    public static StoredDB[] getUserOrderQuantity(int userID) {
        String query = String.format("SELECT orderQuantity FROM `Order` " +
                        "WHERE userID = %d  ",
                userID);

        return Database.getDBObjects(query, Order.class, -1);
    }

    public String toString() {
        return "Order: " +
                "\nOrder ID:" + orderID +
                "\nProduct ID: " + productID +
                "\nuser ID: " + userID +
                "\nSeller ID: " + sellerID +
                "\nShipping Address:'" + shippingAddress +
                "\nOrder Quantity: " + orderQuantity +
                "\n";
    }

    public static String getPrimaryKey() {
        return "orderID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                        "Order (productID, userID, sellerID, shippingAddress, orderQuantity) " +
                        "VALUES ('%d', %d, %d, '%s', %d)",
                productID, userID, sellerID, shippingAddress, orderQuantity);
    }

    @Override
    public String updateQuery() {
        return String.format("UPDATE Product " +
                        "SET productID = '%d', userID = %d, sellerID = %d, shippingAddress = '%s', orderQuantity = %d" +
                        "WHERE orderID = %d",
                productID, userID, sellerID, shippingAddress, orderQuantity);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Order " +
                "WHERE orderID = " + orderID;
    }

}

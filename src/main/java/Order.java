package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {

    private int orderID, productID, customerID, sellerID;
    private String shippingAddress;
    private boolean inDatabase;

    public Order() {
        orderID = 0;
        productID = 0;
        customerID = 0;
        sellerID = 0;
        shippingAddress = "";
    }

    /** Create a Order object with data from database. */
    public Order(int orderID) {
        String query = "SELECT * FROM Order WHERE orderID = " + orderID;
        ResultSet resultSet = null;

        try {
            resultSet = Driver.queryDatabase(query);
            // Throw exception when productID is not found.
            if (!resultSet.isBeforeFirst())
                throw new IllegalArgumentException("OrderID is not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet.next();
            this.orderID = resultSet.getInt("orderID");
            productID = resultSet.getInt("productID");
            customerID = resultSet.getInt("customerID");
            sellerID = resultSet.getInt("sellerID");
            shippingAddress = resultSet.getString("shippingAddress");
            inDatabase = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Order object with all parameters (except reviewID). */
    public Order(int productID, int customerID, int sellerID, String shippingAddress) {
        this.productID = productID;
        this.customerID = customerID;
        this.sellerID = sellerID;
        this.shippingAddress = shippingAddress;
        inDatabase = false;
    }

    // Accesor
    public int getOrderID() { return orderID; }
    public int getProductID() { return productID; }
    public int getCustomerID() { return customerID; }
    public int getSellerID() { return sellerID; }
    public String getShippingAddress() { return shippingAddress; }
    // Mutator
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public void setProductID(int productID) { this.productID = productID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public void setSellerID(int sellerID) { this.sellerID = sellerID; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    /** Add this Order object to database. */
    public void addToDatabase() throws SQLException {

        // Do nothing if the review is already in database.
        if (inDatabase) return;

        String insertQuery = String.format("INSERT INTO " +
                        "Order (productID, customerID, sellerID, shippingAddress) " +
                        "VALUES ('%d', %d, %d, '%s')",
                productID, customerID, sellerID, shippingAddress);
        Driver.updateDatabase(insertQuery);
        inDatabase = true;
    }

    /** Update this Order object in database. */
    public void updateDatabase() throws SQLException {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String updateQuery = String.format("UPDATE Product " +
                        "SET productID = '%d', customerID = %d, sellerID = %d, shippingAddress = '%s' " +
                        "WHERE orderID = %d",
                productID, customerID, sellerID, shippingAddress);
        Driver.updateDatabase(updateQuery);
    }

    /** Delete this Order object in database. */
    public void deleteFromDatabase() throws SQLException {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String deleteQuery = "DELETE FROM Order " +
                "WHERE orderID = " + orderID;
        Driver.updateDatabase(deleteQuery);
    }

    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", productID=" + productID +
                ", customerID=" + customerID +
                ", sellerID=" + sellerID +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", inDatabase=" + inDatabase +
                '}';
    }
}

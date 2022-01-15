package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order extends StoredDB {

    private int orderID, productID, customerID, sellerID;
    private String shippingAddress;

    public Order() {
        orderID = 0;
        productID = 0;
        customerID = 0;
        sellerID = 0;
        shippingAddress = "";
    }

    /** Create an Order object with data from database. */
    public Order(int orderID) {
        String query = "SELECT * FROM Order WHERE orderID = " + orderID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when orderID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("OrderID is not found.");

        try {
            resultSet.next();
            this.orderID = resultSet.getInt("orderID");
            productID = resultSet.getInt("productID");
            customerID = resultSet.getInt("customerID");
            sellerID = resultSet.getInt("sellerID");
            shippingAddress = resultSet.getString("shippingAddress");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Order object with all parameters */
    public Order(int orderID, int productID, int customerID, int sellerID, String shippingAddress) {
        this.orderID = orderID;
        this.productID = productID;
        this.customerID = customerID;
        this.sellerID = sellerID;
        this.shippingAddress = shippingAddress;
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

    public String toString() {
        return "Order: " +
                "\nOrder ID:" + orderID +
                "\nProduct ID: " + productID +
                "\nCustomer ID: " + customerID +
                "\nSeller ID: " + sellerID +
                "\nShipping Address:'" + shippingAddress +
                "\n";
    }

    public String getPrimaryKey() {
        return "orderID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                        "Order (productID, customerID, sellerID, shippingAddress) " +
                        "VALUES ('%d', %d, %d, '%s')",
                productID, customerID, sellerID, shippingAddress);
    }

    @Override
    public String updateQuery() {
        return String.format("UPDATE Product " +
                        "SET productID = '%d', customerID = %d, sellerID = %d, shippingAddress = '%s' " +
                        "WHERE orderID = %d",
                productID, customerID, sellerID, shippingAddress);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Order " +
                "WHERE orderID = " + orderID;
    }

}

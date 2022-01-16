
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;



public class Order extends StoredDB{
    private int orderID,productID,userID,sellerID,orderQuantity;
    private String shippingAddress;
    

public class Order extends StoredDB {

    private int orderID, productID, userID, sellerID, orderQuantity;
    private String shippingAddress;


    public Order() {
        orderID = 0;
        productID = 0;
        userID = 0;
        sellerID = 0;

        orderQuantity = 0;
        shippingAddress = "";
    }

        public int getOrderID() {
            return orderID;
        }

        public int getProductID() {
            return productID;
        }

        public int getUserID() {
            return userID;
        }

        public int getSellerID() {
            return sellerID;
        }

        public int getOrderQuantity() {
            return orderQuantity;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }
        @Override
        public String toString() {
            String details = "\nOrder Notifications~\nOrder ID : " + orderID;
            details = details + "\nUser ID : " + userID;
            details = details + "\nProduct ID :  " + productID;
            details = details + "\nOrder Quantity : " + orderQuantity;
            details = details + "\nShipping Address : " + shippingAddress;
        return details;
             
        }
        
        /** Create a orderNotifications object with data from database. */
    public Order(int sellerID) {
        String query = "SELECT * FROM Orders WHERE sellerID = " + sellerID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when sellerID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("sellerID is not found.");

        try {
            resultSet.next();
            this.sellerID = resultSet.getInt("sellerID");
            orderID = resultSet.getInt("orderID");
            userID = resultSet.getInt("userID");
            productID = resultSet.getInt("productID");
            orderQuantity = resultSet.getInt("orderQuantity");
            shippingAddress = resultSet.getString("shippingAddress");

        shippingAddress = "";
        orderQuantity = 0;
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
            userID = resultSet.getInt("userID");
            sellerID = resultSet.getInt("sellerID");
            shippingAddress = resultSet.getString("shippingAddress");
            orderQuantity = resultSet.getInt("orderQuantity");

            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
     // Create a new Order object with all parameters 
    public Order(int sellerID, int orderID, int userID, int productID, int orderQuantity, String shippingAddress) {
        this.sellerID = sellerID;
        this.orderID = orderID;
        this.userID = userID;
        this.productID = productID;
        this.orderQuantity = orderQuantity;
        this.shippingAddress = shippingAddress;
    }

        // get order by seller ID (for seller)
        public static StoredDB[] getOrderBySellerID(int sellerID) {
           String query = String.format("SELECT * FROM Orders " +
                        "WHERE sellerID = '%d'",
                         sellerID);
           
            return Database.getDBObjects(query, Order.class, -1);
        }
        
        //get order notifications by orderID (for user)
        public static StoredDB[] getOrderByOrderID(int orderID) {
           String query = String.format("SELECT * FROM Orders " +
                        "WHERE orderID = '%d'",
                         orderID);
           
            return Database.getDBObjects(query, Order.class, -1);
        }
        
         //get order notifications by userID (for user)
        public static StoredDB[] getOrderByUserID(int userID) {
           String query = String.format("SELECT * FROM Orders " +
                        "WHERE userID = '%d'",
                         userID);
           
            return Database.getDBObjects(query, Order.class, -1);
        }
        
        //get order notifications based on productID (for seller)
         public static StoredDB[] getOrderByProductID(int productID) {
           String query = String.format("SELECT * FROM Orders " +
                        "WHERE productID = '%d'",
                         productID);
           
            return Database.getDBObjects(query, Order.class, -1);
        }
        
        
        public static String getPrimaryKey() {
        return "sellerID";
    }

        @Override
        public String insertQuery() {
            return String.format("INSERT INTO " + " Orders (orderID, orderID, productID, orderQuantity, shippingAddress)" + 
                                 "VALUES ('%d', '%d','%d','%d', '%s)", orderID, userID, productID, orderQuantity, shippingAddress);
        }

        @Override
        public String updateQuery() {
            return String.format("UPDATE Orders " + "SET orderID ='%d', orderID='%d', productID = '%d', orderQuantity = '%d', shippingAddress = '%s'" +
                                "WHERE sellerID = %d", orderID, orderID, productID, orderQuantity, shippingAddress);
        }

        @Override
        public String deleteQuery() {
            return "DELETE FROM Orders " + "WHERE sellerID = "+ sellerID;
        }
        
        
        
        
        public static void main(String[] args) {

      
  //StoredDB[] Order = getOrderBySellerID(1);  
 //  for (StoredDB i : getOrderBySellerID(1))
 //  System.out.println(i);
   
 //  StoredDB[] Order = getOrderByOrderID(1);  
 //  for (StoredDB i : getOrderByOrderID(1))
  // System.out.println(i);
   
    //StoredDB[] Order = getOrderByUserID(1);  
  // for (StoredDB i : getOrderByUserID(1))
  // System.out.println(i);
  
   StoredDB[] Order = getOrderByProductID(33);  
   for (StoredDB i : getOrderByProductID(33))
   System.out.println(i);

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
        String query = String.format("SELECT orderID FROM Orders " +
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
        String query = String.format("SELECT orderQuantity FROM Orders " +
                        "WHERE userID = %d AND productID = %d ",
                userID, productID);

        return Database.getDBObjects(query, Order.class, -1);
    }

    /** Return the order quantity from the user. */
    public static StoredDB[] getUserOrderQuantity(int userID) {
        String query = String.format("SELECT orderQuantity FROM Orders " +
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

    public String getPrimaryKey() {
        return "orderID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                        "Orders (productID, userID, sellerID, shippingAddress, orderQuantity) " +
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
        return "DELETE FROM Orders " +
                "WHERE orderID = " + orderID;
    }


}

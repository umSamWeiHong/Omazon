//
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;


public class orderNotifications extends StoredDB{
    private int orderID,productID,userID,sellerID,orderQuantity;
    private String shippingAddress;
    
    public orderNotifications() {
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
    public orderNotifications(int sellerID) {
        String query = "SELECT * FROM Orderr WHERE sellerID = " + sellerID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when reviewID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("sellerID is not found.");

        try {
            resultSet.next();
            this.sellerID = resultSet.getInt("sellerID");
            userID = resultSet.getInt("userID");
            productID = resultSet.getInt("productID");
            orderQuantity = resultSet.getInt("orderQuantity");
            shippingAddress = resultSet.getString("shippingAddress");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
        public static StoredDB[] getOrderNotificationsBySellerID(int sellerID) {
           String query = String.format("SELECT * FROM Orderr " +
                        "WHERE sellerID = '%d'",
                         sellerID);
           
            return Database.getDBObjects(query, orderNotifications.class, -1); //orderNotifications.class problem
        }
        
        public static String getPrimaryKey() {
        return "sellerID";
    }

        @Override
        public String insertQuery() {
            return String.format("INSERT INTO " + " Orderr (orderID, orderID, productID, orderQuantity, shippingAddress)" + 
                                 "VALUES ('%d', '%d','%d','%d', '%s)", orderID, userID, productID, orderQuantity, shippingAddress);
        }

        @Override
        public String updateQuery() {
            return String.format("UPDATE Orderr " + "SET orderID ='%d', orderID='%d', productID = '%d', orderQuantity = '%d', shippingAddress = '%s'" +
                                "WHERE sellerID = %d", orderID, orderID, productID, orderQuantity, shippingAddress);
        }

        @Override
        public String deleteQuery() {
            return "DELETE FROM Orderr " + "WHERE sellerID = "+ sellerID;
        }
        
        public static void main(String[] args) {

      
  StoredDB[] orderNotifications = getOrderNotificationsBySellerID(2);  
   for (StoredDB i : getOrderNotificationsBySellerID(2))
   System.out.println(i);
}
        
    
     
}

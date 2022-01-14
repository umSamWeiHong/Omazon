
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Transaction extends StoredDB{
     private int sellerID,userID,orderID;
    private double amount;
    
    public Transaction() {
        sellerID = 0;
        userID = 0;
        orderID = 0;
        amount = 0.0;
    }

        public int getSellerID() {
            return sellerID;
        }

        public int getUserID() {
            return userID;
        }

        public int getOrderID() {
            return orderID;
        }

        public double getAmount() {
            return amount;
        }
    
     @Override
        public String toString() {
            String details = "\nTransactions History for Seller ID : " + sellerID;
            details = details + "\nUser ID : " + userID;
            details = details + "\nOrder ID :  " + orderID;
            details = details + "\nAmount : " + amount;
        return details;
             
        }
        
              /** Create a Transaction object with data from database. */
    public Transaction(int sellerID) {
        String query = "SELECT * FROM TransactionsHistory WHERE sellerID = " + sellerID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when sellerID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("sellerID is not found.");

        try {
            resultSet.next();
            this.sellerID = resultSet.getInt("sellerID");
            userID = resultSet.getInt("userID");
            orderID = resultSet.getInt("orderID");
            amount = resultSet.getDouble("amount");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
        public static StoredDB[] getTransactionBySellerID(int sellerID) {
           String query = String.format("SELECT * FROM TransactionsHistory " +
                        "WHERE sellerID = '%d'",
                         sellerID);
           
            return Database.getDBObjects(query, Transaction.class, -1); 
        }
        
        
        public static String getPrimaryKey() {
        return "sellerID";
    }

        @Override
        public String insertQuery() {
            return String.format("INSERT INTO " + "TransactionsHistory (sellerID, userID, orderID, amount)" + 
                                 "VALUES ('%d', '%d','%d','%f)", sellerID, userID, orderID, amount);
        }

        @Override
        public String updateQuery() {
            return String.format("UPDATE TransactionsHistory " + "SET orderID ='%d', userID='%d', amount = '%f'" +
                                "WHERE sellerID = %d", orderID, userID, amount, sellerID);
        }

        @Override
        public String deleteQuery() {
            return "DELETE FROM TransactionsHistory " + "WHERE sellerID = "+ sellerID;
        }
        
        public static void main(String[] args) {
            StoredDB[] Transaction = getTransactionBySellerID(1);
      for (StoredDB i : getTransactionBySellerID(1))
    System.out.println(i);
        }
    
}


package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;


public class transactionsHistory extends StoredDB{
     private int sellerID,userID,orderID;
    private double profit;
    
    public transactionsHistory() {
        sellerID = 0;
        userID = 0;
        orderID = 0;
        profit = 0.0;
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

        public double getProfit() {
            return profit;
        }
    
     @Override
        public String toString() {
            String details = "\nTransactions History for Seller ID : " + sellerID;
            details = details + "\nUser ID : " + userID;
            details = details + "\nOrder ID :  " + orderID;
            details = details + "\nProfit : " + profit;
        return details;
             
        }
        
              /** Create a transactionshistory object with data from database. */
    public transactionsHistory(int sellerID) {
        String query = "SELECT * FROM TransactionsHistory WHERE sellerID = " + sellerID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when reviewID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("sellerID is not found.");

        try {
            resultSet.next();
            this.sellerID = resultSet.getInt("sellerID");
            userID = resultSet.getInt("userID");
            orderID = resultSet.getInt("orderID");
            profit = resultSet.getDouble("profit");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
        public static StoredDB[] getTransactionsHistoryBySellerID(int sellerID) {
           String query = String.format("SELECT * FROM TransactionsHistory " +
                        "WHERE sellerID = '%d'",
                         sellerID);
           
            return Database.getDBObjects(query, transactionsHistory.class, -1); //transactionsHistory.class problem
        }
        
        
        public static String getPrimaryKey() {
        return "sellerID";
    }

        @Override
        public String insertQuery() {
            return String.format("INSERT INTO " + "TransactionsHistory (sellerID, userID, orderID, profit)" + 
                                 "VALUES ('%d', '%d','%d','%f)", sellerID, userID, orderID, profit);
        }

        @Override
        public String updateQuery() {
            return String.format("UPDATE Product " + "SET orderID ='%d', userID='%d', profit = '%f'" +
                                "WHERE sellerID = %d", orderID, userID, profit, sellerID);
        }

        @Override
        public String deleteQuery() {
            return "DELETE FROM TransactionsHistory " + "WHERE sellerID = "+ sellerID;
        }
        
        public static void main(String[] args) {
            StoredDB[] transactionshistory = getTransactionsHistoryBySellerID(1);
      for (StoredDB i : getTransactionsHistoryBySellerID(1))
    System.out.println(i);
        }
    
}

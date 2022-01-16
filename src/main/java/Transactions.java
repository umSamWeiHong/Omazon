
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Transactions extends StoredDB{
    
    private int sellerID,userID,orderID;
    private double amount;
    
    public Transactions() {
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

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
        
        
    
     @Override
        public String toString() {
            String details = "\nTransactions History : ~ " ;
            details = details + "\nSeller ID : " + sellerID;
            details = details + "\nUser ID : " + userID;
            details = details + "\nOrder ID :  " + orderID;
            details = details + "\nAmount : " + amount;
        return details;
             
        }
        
      /** Create a Transaction object with data from database. */
    public Transactions(int sellerID) {
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
    
     /** Create a new Order object with all parameters */
    public Transactions(int sellerID, int orderID, int userID, double amount) {
        this.sellerID = sellerID;
        this.orderID = orderID;
        this.userID = userID;
        this.amount = amount;

    }
    
    /** Return the N recent transactions history from the user. */
    public static StoredDB[] getUserTransactionsHistory(int userID, int N) {
        String query = String.format("SELECT orderID FROM TransactionsHistory " +
                        "WHERE userID = %d ",
                userID);

        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Transactions.class, N);
    }
    
    /** Return all transactions from the user. */
    public static StoredDB[] getUserTransactionsHistory(int userID) {
        return getUserTransactionsHistory(userID, -1);
    }
    
     // get transactions by seller ID  
        public static StoredDB[] getTransactionsBySellerID(int sellerID) {
           String query = String.format("SELECT * FROM TransactionsHistory " +
                        "WHERE sellerID = '%d'",
                         sellerID);
           
            return Database.getDBObjects(query, Transactions.class, -1); 
        }
        //get transactions based on orderID 
        public static StoredDB[] getTransactionsByOrderID(int orderID) {
           String query = String.format("SELECT * FROM TransactionsHistory " +
                        "WHERE orderID = '%d'",
                         orderID);
           
            return Database.getDBObjects(query, Transactions.class, -1); 
        }
        
        //get transactions based on userID
         public static StoredDB[] getTransactionByUserID(int userID) {
           String query = String.format("SELECT * FROM TransactionsHistory " +
                        "WHERE userID = '%d'",
                         userID);
           
            return Database.getDBObjects(query, Transactions.class, -1); 
        }
         
         /** Return the amount from a specific userID. */
        public static StoredDB[] getUserAmount(int userID) {
        String query = String.format("SELECT amount FROM TransactionsHistory " +
                        "WHERE userID = %d  ",
                userID);

        return Database.getDBObjects(query, Transactions.class, -1);
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
            
     // StoredDB[] Transactions = getTransactionsByOrderID(1);
     //for (StoredDB i : getTransactionsByOrderID(1))
     // System.out.println(i);
       
       StoredDB[] Transactions = getTransactionByUserID(1);
       for (StoredDB i : getTransactionByUserID(1))
       System.out.println(i);
      
      // StoredDB[] Transactions = getTransactionsBySellerID(3);
     // for (StoredDB i : getTransactionsBySellerID(3))
     //  System.out.println(i);
      
     //  StoredDB[] Transactions = getUserTransactionsHistory(3);
     //  for (StoredDB i : getUserTransactionsHistory(3))
     //  System.out.println(i);
     
      
        }
}

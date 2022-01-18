
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction extends StoredDB{

    private int transactionID, sellerID, userID, orderID;
    private double amount;

    /** Create a Transaction object with data from database. */
    public Transaction(int transactionID) {
        String query = "SELECT * FROM Transaction WHERE transactionID = " + transactionID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when transactionID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("transactionID is not found.");

        try {
            resultSet.next();
            this.transactionID = resultSet.getInt("transactionID");
            this.sellerID = resultSet.getInt("sellerID");
            userID = resultSet.getInt("userID");
            orderID = resultSet.getInt("orderID");
            amount = resultSet.getDouble("amount");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Transaction(int sellerID, int userID, int orderID, double amount) {
        this.sellerID = sellerID;
        this.orderID = orderID;
        this.userID = userID;
        this.amount = amount;
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

    public int getTransactionID() {
        return transactionID;
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

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {
        String details = "\nTransactions History : ~ " ;
        details = details + "\nTransactionsID : " + transactionID;
        details = details + "\nOrder ID :  " + orderID;
        details = details + "\nSeller ID : " + sellerID;
        details = details + "\nUser ID : " + userID;
        details = details + "\nAmount : " + amount;
        return details;
    }

    /** Return the N recent transactions from the user. */
    public static StoredDB[] getUserTransactions(int userID, int N) {
        String query = String.format("SELECT orderID FROM TransactionsHistory " +
                        "WHERE userID = %d ",
                userID);

        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Transaction.class, N);
    }

    /** Return all transactions from the user. */
    public static StoredDB[] getUserTransactions(int userID) {
        return getUserTransactions(userID, -1);
    }

    // get transactions by seller ID
    public static StoredDB[] getTransactionsBySellerID(int sellerID) {
        String query = String.format("SELECT * FROM TransactionsHistory " +
                                "WHERE sellerID = '%d'",
                                sellerID);
        return Database.getDBObjects(query, Transaction.class, -1);
    }

    public static String getPrimaryKey() {
        return "transactionID";
    }

    @Override
    public String insertQuery() {
        return String.format("""
                        INSERT INTO Transaction (sellerID, userID, orderID, amount)
                         VALUES (%d, %d, %d, %.2f)""",
                        sellerID, userID, orderID, amount);
    }

    @Override
    public String updateQuery() {
        return String.format("""
                        UPDATE Transaction
                         SET sellerID = %d, orderID = %d, userID= %d, amount = %.2f
                         WHERE transactionID = %d""",
                        sellerID, orderID, userID, amount, transactionID);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Transaction WHERE transactionID = " + transactionID;
    }
}

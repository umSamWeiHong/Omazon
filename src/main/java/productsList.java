
package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class productsList extends StoredDB{
    private int productID,stock, sellerID;
    private double price;
    private String productName;
    
    public productsList() {
        productID = 0;
        productName = "";
        price = 0.0;
        stock = 0;   
    }

        public int getProductID() {
            return productID;
        }

        public int getStock() {
            return stock;
        }

        public int getSellerID() {
            return sellerID;
        }

        public double getPrice() {
            return price;
        }

        public String getProductName() {
            return productName;
        }
        
        @Override
        public String toString() {
            String details = "\nProduct Name : " + productName;
            details = details + "\nPrice : " + price;
            details = details + "\nStock :  " + stock;
            details = details + "\nProductID : " + productID;
        return details;
             
        }
        
        public productsList(int sellerID) {
        String query = "SELECT * FROM Product WHERE sellerID = " + sellerID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when sellerID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("sellerID is not found.");

        try {
            resultSet.next();
            this.sellerID = resultSet.getInt("sellerID");
            productID = resultSet.getInt("productID");
            productName = resultSet.getString("productName");
            price = resultSet.getInt("price");
            stock = resultSet.getInt("stock");
            
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
        public static StoredDB[] getProductsListBySellerID(int sellerID) {
           String query = String.format("SELECT * FROM Product " +
                        "WHERE sellerID = '%d'",
                         sellerID);
           
            return Database.getDBObjects(query, Product.class, -1);
        }
        
        public static String getPrimaryKey() {
        return "sellerID";
    }
    

        @Override
        public String insertQuery() {
            return String.format("INSERT INTO " + "Product (productId, productName, stock, price)" + 
                                 "VALUES ('%d', '%s','%d','%f)", productID, productName,stock,price);
        }

        @Override
        public String updateQuery() {
            return String.format("UPDATE Product " + "SET productName ='%s', stock='%d', price = '%f'" +
                                "WHERE productID = %d", productName, stock,price, productID);
        }

        @Override
        public String deleteQuery() {
            return "DELETE FROM Product " + "WHERE ProductID = "+ productID;
        } 
        
        public static void main(String[] args) {
       StoredDB[] products = getProductsListBySellerID(3);  //passed
      for (StoredDB i : getProductsListBySellerID(3))
       System.out.println(i);
        
}
}


    

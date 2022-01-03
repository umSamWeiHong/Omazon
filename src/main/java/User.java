package main.java;

public class User {

    private String username;
    private String email;
    private String password;

    // Customer aspect
    private double balance;
    private Product[] cartProduct;
    private String[] orderHistory;
    private String paymentPassword;

    // Seller
    private double profit;
    private Product[] productsList;
    private String[] transactionsHistory;
    private String[] orderNotifications;
    
      public Seller(int productID, String productName, Double price, int stock, int sales, Double productRatings, String description) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.productRatings = productRatings;
        this.description = description;
        inDatabase = false;
    } 
      // Accessor
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public int getSales() {
        return sales;
    }
    public Double getProductRatings() {
        return productRatings;
    }
    public String[] getReviews() {
        return reviews;
    }
    public String getProductName() {
        return productName;
    }

     /** Add this Product object to database. */
    public void addToDatabase() throws SQLException {

        // Do nothing if the review is already in database.
        if (inDatabase) return;

        String insertQuery = String.format("INSERT INTO " +
                        "Product (productName, price, stock, sales, description) " +
                        "VALUES ('%s', %f, %d, %d, '%s')",
                productName, price, stock, sales, description);
        Driver.updateDatabase(insertQuery);
        inDatabase = true;
    }
    /** Update this Product object in database. */
    public void updateDatabase() throws SQLException {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String updateQuery = String.format("UPDATE Product " +
                        "SET productName = '%s', price = %f, stock = %d, sales = %d, description = '%s' " +
                        "WHERE productID = %d",
                productName, price, stock, sales, description);
        Driver.updateDatabase(updateQuery);
    }

    /** Delete this Product object in database. */
    public void deleteFromDatabase() throws SQLException {

        // Do nothing if the review is not in database.
        if (!inDatabase) return;

        String deleteQuery = "DELETE FROM Product " +
                "WHERE productID = " + productID;
        Driver.updateDatabase(deleteQuery);
    }
    
    public static Product[] getProductsList() {
        String query;
        query = String.format("SELECT * FROM SellerInventory" + "ORDER BY productID");
         ResultSet resultSet = null;
        try {
            resultSet = Driver.queryDatabase(query);
            if (!resultSet.isBeforeFirst())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String toString() {
        String details = "Product name:" + productName;
        details = details + "\nPrice: " + price;
        details = details + "\nStock: " + stock;
        details = details + "\nSales: " + sales;
        details = details + "\nProduct ratings: " + productRatings;
        details = details + "\nDescription: " + description;
        details = details + "\nProduct reviews: " + reviews;
        return details;
    }    
   
    
    private String[] transactionsHistory;
    
    private String[] orderNotifications;
}
}

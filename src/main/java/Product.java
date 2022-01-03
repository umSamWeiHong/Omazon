package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Product {

    private int productID;
    private String productName;
    private double price;
    private int stock;
    private int sales;
    private double productRatings;
    private String description;
    private String[] reviews;
    private boolean bestSelling;
    private boolean inDatabase;


    public Product() {
        productName = "";
        price = 0.0;
        stock = 0;
        sales = 0;
        productRatings = 0.0;
        description = "";
        reviews = new String[10];
    }

    /** Create a Product object with data from database. */
    public Product(int productID) {
        String query = "SELECT * FROM Product WHERE productID = " + productID;
        ResultSet resultSet = null;

        try {
            resultSet = Driver.queryDatabase(query);
            // Throw exception when productID is not found.
            if (!resultSet.isBeforeFirst())
                throw new IllegalArgumentException("ProductID is not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet.next();
            this.productID = resultSet.getInt("productID");
            productName = resultSet.getString("productName");
            price = resultSet.getDouble("price");
            stock = resultSet.getInt("stock");
            sales = resultSet.getInt("sales");
            productRatings = resultSet.getDouble("productRatings");
            description = resultSet.getString("description");

            inDatabase = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /** Create a new Product object with all parameters */
    public Product(int productID, String productName, Double price, int stock, int sales, Double productRatings, String description) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.productRatings = productRatings;
        this.description = description;
        inDatabase = false;
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




    public Product(String productName, String description, double price, int stock, int sales, String[] reviews) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.reviews = reviews;
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
    public boolean isBestSelling() {
        return bestSelling;
    }
    // Mutator
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setDecsription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setSales(int sales) {
        this.sales = sales;
    }
    public void setProductRatings(Double productRatings) {
        this.productRatings = productRatings;
    }
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }
    public void setBestSelling(boolean bestSelling) {
        this.bestSelling = bestSelling;
    }
    @Override
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

    /** Method to get 3 best selling products based on salescount*/
    public static Product[] getBestSelling() {
        String query = String.format("SELECT * FROM Product " +
                        "ORDER BY sales DESC " +
                        "LIMIT 3");

        ResultSet resultSet = null;
        try {
            resultSet = Driver.queryDatabase(query);
            if (!resultSet.isBeforeFirst())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Product[] product = new Product[3];
        try {
            int index = 0;
            while (resultSet.next()) {
                product[index] = new Product(resultSet.getInt("productID"));
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    /** Method to get products by title */
    public static Product[] getProductsByTitle(String title) {
        String query = String.format("SELECT * FROM Product " +
                "WHERE productName LIKE '%%%s%%'  ",
                title);

        ResultSet resultSet = null;
        try {
            resultSet = Driver.queryDatabase(query);
            if (!resultSet.isBeforeFirst())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            // Initialize the array with number of results.
            resultSet.last();
            Product[] product = new Product[resultSet.getRow()];
            resultSet.beforeFirst();

            int index = 0;
            while (resultSet.next()) {
                product[index] = new Product(resultSet.getInt("productID"));
                index++;
            }
            return product;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Method to get products by category */
    public static Product[] getProductsByCategory(String category) {
        String query = String.format("SELECT * FROM Category " +
                "JOIN Product " +
                "ON Category.productID = Product.productID " +
                "WHERE categoryName = '%s'",
                category);


        ResultSet resultSet = null;
        try {
            resultSet = Driver.queryDatabase(query);
            if (!resultSet.isBeforeFirst())
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            // Initialize the array with number of results.
            resultSet.last();
            Product[] product = new Product[resultSet.getRow()];
            resultSet.beforeFirst();

            int index = 0;
            while (resultSet.next()) {
                product[index] = new Product(resultSet.getInt("productID"));
                index++;
            }
            return product;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
       /* Product product1 = new Product(0, "cake", 25.50, 100, 0, 0.0, "YUMMY");
        try {
            product1.addToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

       /*Product product[] = getBestSelling();
      for(int i = 0; i < product.length;i++) {
         System.out.println(product[i]);

       } */

        /*Product product[] = getProductsByTitle("Sports and Outdoor");
        for(int i = 0; i < product.length;i++) {
        System.out.println(product[i]);

        } */

        Product product[] = getProductsByCategory(Category.FASHION_ACCESSORIES.displayName());
        for(int i = 0; i < product.length;i++) {
            System.out.println(product[i]);
        }

    }
}
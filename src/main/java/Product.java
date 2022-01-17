package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Product extends StoredDB {

    private int productID, stock, sales, sellerID;
    private String productName, description, image;
    private double price, productRatings;
    private Category category;
    private String[] reviews;
    private boolean bestSelling;

    public Product() {
        productName = "";
        price = 0.0;
        stock = 0;
        sales = 0;
        productRatings = 0.0;
        description = "";
        category = Category.valueOf("");
        image = "";
        reviews = new String[10];
    }

    /** Create a Product object with data from database. */
    public Product(int productID) {
        String query = "SELECT * FROM Product WHERE productID = " + productID;
        ResultSet resultSet = Database.queryDatabase(query);
        // Throw exception when productID is not found.
        if (resultSet == null)
            throw new IllegalArgumentException("ProductID is not found");

        try {
            resultSet.next();
            this.productID = resultSet.getInt("productID");
            productName = resultSet.getString("productName");
            price = resultSet.getDouble("price");
            stock = resultSet.getInt("stock");
            sales = resultSet.getInt("sales");
            productRatings = resultSet.getDouble("productRatings");
            description = resultSet.getString("description");
            category = Category.valueOf(resultSet.getString("category"));
            sellerID = resultSet.getInt("sellerID");
            image = resultSet.getString("image");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Product object with all parameters */
    public Product(int productID, String productName, Double price, int stock, int sales, Double productRatings, String description, Category category, int sellerID, String base64String) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.productRatings = productRatings;
        this.description = description;
        this.category = category;
        this.sellerID = sellerID;
        this.image = base64String;
    }

    public Product(String productName, String description, double price, int stock, int sales, String[] reviews, Category category, int sellerID, String base64String) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.reviews = reviews;
        this.category = category;
        this.sellerID = sellerID;
        this.image = base64String;
    }

    /** Add product from GUI */
    public Product(String productName, String description, double price, int stock, Category category, String imgBase64String, int sellerID) {
        this(productName, description, price, stock, 0, null, category, sellerID, imgBase64String);
    }

    // Accessor
    public int getProductID() {
        return productID;
    }

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

    public Category getCategory() {
        return category;
    }
    public int getSellerID() {
        return sellerID;
    }
    public String getBase64String() {
        return image;
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
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }
    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }
    public void setImage(String base64String) {
        this.image = base64String;
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

    public static String validateProductInformation(String productName, String price, String stock) {

        if (productName.equals(""))
            return "Product name cannot be empty.";
        if (price.equals(""))
            return "Price cannot be empty.";

        try {
            if (Double.parseDouble(price) < 0)
                return "Price cannot be a negative number";
        } catch (NumberFormatException e) {
            return "Please enter a valid number for price.";
        }

        if (stock.equals(""))
            return "Stock cannot be empty.";

        try {
            if (Integer.parseInt(stock) < 0)
                return "Stock cannot be a negative number";
        } catch (NumberFormatException e) {
            return "Please enter a valid number for stock.";
        }

        return "OK";
    }

    /** Method to get 3 best-selling products based on salescount */
    public static StoredDB[] getBestSelling() {
        String query = String.format("SELECT * FROM Product " +
                "ORDER BY sales DESC " +
                "LIMIT 3");
        return Database.getDBObjects(query, Product.class, 3);
    }

    /** Method to get products by title */
    public static StoredDB[] getProductsByTitle(String title) {
        String query = String.format("SELECT * FROM Product " +
                        "WHERE productName LIKE '%%%s%%'  ",
                title);

        return Database.getDBObjects(query, Product.class, -1);
    }

    /** Method to get products by category */
    public static StoredDB[] getProductsByCategory(Category category) {
        String query = String.format("SELECT * FROM Product " +
                        "WHERE category = '%s'",
                category);

        return Database.getDBObjects(query, Product.class, -1);
    }

    /** Method to get products based on sellerID */
    public static StoredDB[] getProductsBySellerId(int sellerID) {
        String query = String.format("SELECT * FROM Product " +
                        "WHERE sellerID = %d  ",
                sellerID);

        return Database.getDBObjects(query, Product.class, -1);
    }

    public static Product[] getProductsByProductIDs(int[] productIDs) {
        int N = productIDs.length;
        Product[] products = new Product[N];
        String ids = Arrays.toString(productIDs);

        String query = "SELECT * FROM Product WHERE productID IN (" +
                       ids.substring(1, ids.length()-1) + ")";
        ResultSet resultSet = Database.queryDatabase(query);

        if (resultSet == null)
            return null;

        int count = 0;
        try {
            while (resultSet.next()) {
                int productID = resultSet.getInt("productID");
                String productName = resultSet.getString("productName");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                int sales = resultSet.getInt("sales");
                double productRatings = resultSet.getDouble("productRatings");
                String description = resultSet.getString("description");
                Category category = Category.valueOf(resultSet.getString("category"));
                int sellerID = resultSet.getInt("sellerID");
                String image = resultSet.getString("image");
                products[count++] = new Product(productID, productName, price, stock, sales, productRatings, description, category, sellerID, image);
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPrimaryKey() { return "productID"; }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                        "Product (productName, price, stock, sales, description, category, sellerID, image) " +
                        "VALUES ('%s', %f, %d, %d, '%s', '%s', %d, '%s')",
                productName, price, stock, sales, description, category, sellerID, image);
    }

    @Override
    public String updateQuery() {
        return String.format("UPDATE Product " +
                        "SET productName = '%s', price = %.2f, stock = %d, sales = %d, description = '%s', category = '%s', sellerID = %d " +
                        "WHERE productID = %d",
                        productName, price, stock, sales, description, category, sellerID, productID);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Product " +
                "WHERE productID = " + productID;
    }
}

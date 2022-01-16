package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            category = Category.valueOf(Category.getEnum(resultSet.getString("category")));
            sellerID = resultSet.getInt("sellerID");
            image = resultSet.getString("image");

            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Product object with all parameters */
    public Product(int productID, String productName, Double price, int stock, int sales, Double productRatings, String description, String category, int sellerID, String base64String) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.productRatings = productRatings;
        this.description = description;
        this.category = Category.valueOf(category);
        this.sellerID = sellerID;
        this.image = base64String;

    }

    public Product(String productName, String description, double price, int stock, int sales, String[] reviews, String category, int sellerID, String base64String) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
        this.reviews = reviews;
        this.category = Category.valueOf(category);
        this.sellerID = sellerID;
        this.image = base64String;
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

    /** Method to resize image */
    public static void resize(String inputImagePath, String outputImagePath) throws IOException {
        // Reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // Creates output image
        BufferedImage outputImage = new BufferedImage(300,
                300, inputImage.getType());

        // Scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, 300, 300, null);
        g2d.dispose();

        // Extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // Writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
    /** Method to convert image into Base64 string*/
    public static String imgToBase64String(String path) throws FileNotFoundException, IOException {
        File img = new File(path);
        FileInputStream input = new FileInputStream(img);
        byte[] bytes = new byte[(int) img.length()];
        input.read(bytes);
        return new String(Base64.getEncoder().encode(bytes));
    }

    /** Method to convert Base64 string to image*/
    public static byte[] base64StringToImage(String base64){
        byte[] data = Base64.getDecoder().decode(base64);
        return data;
        /*
        //IF Write to destination path
        try ( OutputStream stream = new FileOutputStream("C:/Users/kelee/Documents/ImageProduct/image_destination.jpg")) {
            stream.write(data);

        } catch (Exception e) {
            System.out.println("Couldn't write to file");

        }
         */
    }

    public static void main(String[] args) throws IOException  {

        /*StoredDB[] products = getBestSelling();
        for (StoredDB i : getBestSelling())
            System.out.println(i); */

        /*StoredDB[] products = getProductsByTitle("toothpaste");
        for (StoredDB i : getProductsByTitle("toothpaste"))
            System.out.println(i); */

        /*
        StoredDB[] products = getProductsByCategory(Category.FASHION_ACCESSORIES);
        for (StoredDB i : getProductsByCategory(Category.FASHION_ACCESSORIES))
            System.out.println(i); */


        /*
        StoredDB[] products = getProductsBySellerId(1);
        for (StoredDB i : getProductsBySellerId(1))
            System.out.println(i); */

        /*
        String inputImagePath = "C:/Users/kelee/Documents/ImageProduct/17.jpg";
        String outputImagePath1 = "C:/Users/kelee/Documents/ImageProduct/17_Fixed.jpg";

        try {
            // resize to a fixed height and width(300x300)
            Product.resize(inputImagePath, outputImagePath1);

        } catch (IOException ex) {
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }*/

        //String base64String = imgToBase64String("C:\\Users\\kelee\\Documents\\ImageProduct\\image.jpg");
        //System.out.println(base64String);
        //base64StringToImage(base64String);


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
                        "SET productName = '%s', price = %f, stock = %d, sales = %d, description = '%s', category = '%s', sellerID = %d, image = '%s' " +
                        "WHERE productID = %d",
                productName, price, stock, sales, description, category, sellerID, image);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM Product " +
                "WHERE productID = " + productID;
    }
}

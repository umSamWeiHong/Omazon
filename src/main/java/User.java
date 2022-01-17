package main.java;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.imageio.ImageIO;
import java.util.Base64;

public class User extends StoredDB {

    private int userID;
    private String username;
    private String email;
    private String password;
    private String image;

    // Customer aspect
    private double balance;
    private Product[] cartProduct;
    private String[] orderHistory;
    private String paymentPassword, shippingAddress;

    // Seller
    private double profit;
    private Product[] productsList;
    private String[] transactionsHistory;
    private String[] orderNotifications;

    private Timestamp dateCreated;

    /** Create a User object with data from database. */
    public User(int userID) {
        String query = "SELECT * FROM User WHERE userID = " + userID;
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            throw new IllegalArgumentException("userID is not found.");

        try {
            resultSet.next();
            this.userID = resultSet.getInt("userID");
            username = resultSet.getString("username");
            email = resultSet.getString("email");
            password = resultSet.getString("password");
            balance = resultSet.getDouble("balance");
            profit = resultSet.getDouble("profit");
            paymentPassword = resultSet.getString("paymentPassword");
            shippingAddress = resultSet.getString("shippingAddress");
            dateCreated = resultSet.getTimestamp("dateCreated");
            image = resultSet.getString("image");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User(String username, String email, String password, String image) {
        this(username, email, password, image, null);
    }

    /** Create a new User object with all parameters (except userID). */
    public User(String username, String email, String password, Timestamp dateCreated, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateCreated = dateCreated;
        this.image = base64String;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public Product[] getCartProduct() {
        return cartProduct;
    }

    public String[] getOrderHistory() {
        return orderHistory;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public double getProfit() {
        return profit;
    }

    public Product[] getProductsList() {
        return productsList;
    }

    public String[] getTransactionsHistory() {
        return transactionsHistory;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String[] getOrderNotifications() {
        return orderNotifications;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = base64String;
    } 

    /** Edit the user details. */
    public void editDetails(String username, String email, String password, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = base64String;
    }

    @Override
    public String toString() {
        return "user{" +
                "userID =" + userID +
                ", username =" + username +
                ", password =" + password +
                ", email =" + email +
                ", create_time =" + dateCreated +
                ", image =" + image +
                ", inDatabase=" + inDatabase() +
                '}';
    }

    public void topUp(double amount) {
        balance = amount;
        Database.update(this);
    }

    public void addToCart(int productID) {
        Database.add(new Cart(userID, productID));
    }

    public void addToFavourites(int productID) {
        Database.add(new Favourite(userID, productID));
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
        System.out.println(base64);
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

    public static void main(String[] args) {
        System.out.println(new User(2));
    }

    public static String getPrimaryKey() {
        return "userID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                        "`User` (username, email, password) " +
                        "VALUES ('%s', '%s', '%s')",
                        username, email, password);
    }

    @Override
    public String updateQuery() {
        return String.format("""
                        UPDATE `User`
                         SET username = '%s', email = '%s', password = '%s', balance = %.2f, shippingAddress = '%s',
                         paymentPassword = '%s', image = '%s'
                         WHERE userID = %d""",
                        username, email, password, balance, shippingAddress, paymentPassword, userID,image);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM `User` " +
               "WHERE userID = " + userID;
    }
}

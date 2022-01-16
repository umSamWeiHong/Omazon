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
    
    public User(String username, String email, String password, double balance, Product[] cartProduct, String[] orderHistory, String paymentPassword, double profit, Product[] productsList, String[] transactionsHistory, String[] orderNotifications) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.cartProduct = cartProduct;
        this.orderHistory = orderHistory;
        this.paymentPassword = paymentPassword;
        this.profit = profit;
        this.productsList = productsList;
        this.transactionsHistory = transactionsHistory;
        this.orderNotifications = orderNotifications;
    }

    /**Accessor and mutator*/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Product[] getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(Product[] cartProduct) {
        this.cartProduct = cartProduct;
    }

    public String[] getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(String[] orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public Product[] getProductsList() {
        return productsList;
    }

    public void setProductsList(Product[] productsList) {
        this.productsList = productsList;
    }

    public String[] getTransactionsHistory() {
        return transactionsHistory;
    }

    public void setTransactionsHistory(String[] transactionsHistory) {
        this.transactionsHistory = transactionsHistory;
    }

    public String[] getOrderNotifications() {
        return orderNotifications;
    }

    public void setOrderNotifications(String[] orderNotifications) {
        this.orderNotifications = orderNotifications;
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
    
    
}

}


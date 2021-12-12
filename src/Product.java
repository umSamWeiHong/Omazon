import java.util.ArrayList;
import java.util.Scanner;

class Product {
    private String name;
    private double price;
    private int quantity;
    private int itemNumber;
    private boolean productStatus = true;

    public Product() { // Default Empty Constructor
    }

    public Product(String name, double price, int quantity, int itemNumber) {
        this.name = name;
        this.itemNumber = itemNumber;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getItemNumber() {
        return this.itemNumber;
    }

    public boolean getProductStatus() {
        return this.productStatus;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    public double calculateValue() { // Method to calculate the value of the product
        return this.price * this.quantity;
    }

    public String toString() { // toString method to return the product details
        return "Item Number \t\t:" + this.getItemNumber() + "\nName \t\t:" + this.getName() + "\nPrice \t\t:"
                + this.getPrice() + "\nQuantity in Stock \t\t:" + this.getQuantity() + "\nStock Value \t\t:"
                + this.calculateValue();
    }
}

class prdct extends Product {    // PRODUCT
    private String description;
    private int sales;
    private String ratings;


    public prdct(String name, double price, int quantity, int itemNumber, String description, int sales, String ratings) { // Parameterized
        // constructor
        // that
        // calls
        // super
        super(name, price, quantity, itemNumber);
        this.description = description;
        this.sales = sales;
        this.ratings = ratings;
    }

    // Getters
    public String getDescription() {
        return this.description;
    }

    public int getSales() {
        return this.sales;
    }

    public String getRatings() {
        return this.ratings;
    }


    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String toString() { // Overriding the toString method to display it's own stuff
        String status;
        if (this.getProductStatus())
            status = "Active";
        else
            status = "Not Active";
        return "Item Number\t: " + this.getItemNumber() + "\nProduct Name\t: " + this.getName() + "\nPrice\t: RM " + this.getPrice() + "\nIn Stock: " + this.getQuantity() + "\nSales\t: " + this.getSales() +"\nEstimated Total Profit Value\t:" + this.calculateValue() + "\nProduct Ratings\t: " + this.getRatings()+ "\nDescription\t:\n"
                + this.getDescription()   +"\nProduct Status\t: " + status;
    }
}

class ProductTester { // Class to test all the classes made

    public static void addProduct(int type, ArrayList<Product> inventory) { // Takes in the type of the product, and the
        // inventory list and prompts user to enter
        // the details accordingly and adds it to
        // the ArrayList
        Scanner R = new Scanner(System.in);
        System.out.print("Please enter the item number you wish to register this product as\t: ");
        int itemNumber = R.nextInt();
        Scanner w = new Scanner(System.in); //remove enter buffer
        System.out.print("Please enter the product name\t: ");
        String name = w.nextLine(); //remove enter buffer
        System.out.print("Please enter the price of this product (RM)\t: ");
        double price = w.nextDouble();
        System.out.print("Please enter the quantity of stock for this product\t: ");
        int quantity = w.nextInt();
        System.out.print("Please enter the sales : ");
        int sales = w.nextInt();
        Scanner s = new Scanner(System.in); //remove enter buffer
        System.out.print("Please enter ratings: ");
        String ratings = s.nextLine();
        System.out.print("Please enter the description of the product\t: ");
        String description = s.nextLine();

        prdct ctemp = new prdct(name, price, quantity, itemNumber, description,sales, ratings);
        inventory.add(ctemp);
        System.out.println();
    }


    public static void main(String at[]) throws Exception { // Main program to test the above functions
        Scanner R = new Scanner(System.in);
        System.out.println("---------------------------");
        System.out.println("\tOmazon\t\t");
        System.out.println("---------------------------");
        System.out.println("Welcome to the Seller Page! Before we start of, would you like to add any new products today?");
        System.out.println("If Yes then please enter the number of products you would like to add.\nEnter 0(zero) if you do not wish to add any products: ");
        int numOfProducts = R.nextInt();
        System.out.println();

        ArrayList<Product> inventory = new ArrayList<Product>(); // We will be using arraylist of Product type to store
        // the inventory

        for (int i = 0; i < numOfProducts; i++) { // Prompting the user for product type for each product
            System.out.print("Type '1' to continue : ");
            int type = R.nextInt();
            System.out.println();

            addProduct(type, inventory);
        }

        System.out.println("\nInventory has been successfully updated.");
        int choice = 1;
        while (choice != 0) { // A menu based loop that asks the user the operations that can be done with the
            // Inventory
            System.out.println("\n\t1. View Inventory\n\t2. Add Stock\n\t3. Deduct Stock\n\t4. Discontinue Product\n\t5. Transactions History\n\t6. Edit Existing Product\n\t0. Logout\n");
            System.out.print("Please enter a menu option: ");
            choice = R.nextInt();

            switch (choice) {
                case 1: // If the user wants to view the inventory
                    if (inventory.size() == 0) // For an empty inventory
                        System.out.println("The list is still empty, please add products to view the inventory.");
                    else {
                        System.out.println("\nPrinting the details of the Inventory: ");
                        for (int i = 0; i < inventory.size(); i++) {
                            System.out.println(inventory.get(i).toString()); // Using each class's toString function to
                            // print the details
                            System.out.println();
                        }
                    }
                    break;
                case 2: // if the user wants to add to the inventory
                    System.out.print("\nType '1' to continue : ");
                    int type = R.nextInt();
                    addProduct(type, inventory); // Using the previously defined function to add to the arraylist
                    System.out.println("Product has been added successfully\n");
                    break;
                case 3: // If the user wants to delete from the inventory, takes the item name and item
                    // number to find the product
                    System.out.print("Enter the name of the product to be deleted: ");
                    R.nextLine();
                    String name = R.nextLine();
                    System.out.print("Enter the Item Number of the product to be deleted: ");
                    int itemNumber = R.nextInt();
                    int x = 0;
                    for (x = 0; x < inventory.size(); x++) {
                        if (inventory.get(x).getName().equalsIgnoreCase(name)
                                && inventory.get(x).getItemNumber() == itemNumber) {
                            System.out.println("Found the product, deleting it....");
                            System.out.println("Product successfully deleted and removed from inventory.\n");
                            inventory.remove(x);
                            break;
                        }
                    }
                    if (x == inventory.size() + 1) // If product not found, do nothing
                        System.out.println("Product not found or does not exist.\n");
                    break;
                case 4: // If the user wants to discontinue a product, applies the same logic as before
                    // to find the product
                    System.out.print("Enter the name of the product to be discontinued: ");
                    R.nextLine();
                    name = R.nextLine();
                    System.out.print("Enter the Item Number of the product to be discontinued: ");
                    itemNumber = R.nextInt();
                    for (x = 0; x < inventory.size(); x++) {
                        if (inventory.get(x).getName().equalsIgnoreCase(name)
                                && inventory.get(x).getItemNumber() == itemNumber) {
                            inventory.get(x).setProductStatus(false);
                            break;
                        }
                    }
                    if (x == inventory.size() + 1)
                        System.out.println("Product not found or does not exist.\n");
                    break;

                case 5 : //transactions history
                    System.out.println("Transactions History\n\n\n");

                    break;

                case 6 :
                    System.out.println("Editing products\n");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid command. Please try again.\n\n");
            }
        }
    }
}

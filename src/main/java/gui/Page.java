package main.java.gui;

public enum Page {
    LOGIN ("Login"),
    REGISTER ("Register"),
    HOME ("Home"),
    PROFILE ("Profile"),
    PRODUCT ("Product"),
    STORE ("Store"),
    CART ("Cart"),
    ORDER ("Order"),
    FAVOURITE ("Favourite"),
    SEARCH ("Search"),
    SETTINGS ("Settings"),
    EXPLORE ("Explore");

    private final String filename;

    Page(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}

package main.java.gui;

public enum Page {
    LOGIN ("Login"),
    PROFILE ("Profile");

    private final String filename;

    Page(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
    // e
}

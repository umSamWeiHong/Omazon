package main.java;

public enum Category {

    HEALTH_AND_BEAUTY("Health and Beauty"),
    TOILETRIES("Toiletries"),
    SPORTS_AND_OUTDOOR("Sports and outdoor"),
    FASHION_ACCESSORIES("Fashion Accessories");

    private String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }


    @Override
    public String toString() {
        return displayName;
    }
}

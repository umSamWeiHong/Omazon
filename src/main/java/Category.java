package main.java;

public enum Category {

//    NULL(""),
    HEALTH_AND_BEAUTY("Health and Beauty"),
    TOILETRIES("Toiletries"),
    SPORTS_AND_OUTDOOR("Sports and Outdoor"),
    FASHION_ACCESSORIES("Fashion Accessories");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}

package main.java;

import java.util.Arrays;

public class Main {

    private static User user = new User(1);

    public static void main(String[] args) {
        System.out.println("Hello Omazon!");
    }


    public static User getUser() {
        return user;
    }

    public static void setUser(int userID) {
        user = new User(userID);
    }
}

package main.java;

import java.util.Arrays;

public class Main {

    private static User user = new User(1);

    public static void main(String[] args) {
        System.out.println("Hello Omazon!");

//        user.updateCartIDs();
        user.updateFavouriteIDs();
//        user.updateOrderIDs();
//        System.out.println(Arrays.toString(user.getCartIDs()));
//        System.out.println(Arrays.toString(user.getOrderIDs()));
        user.updateProductIDs();
        System.out.println(Arrays.toString(user.getProductIDs()));
//        Cart cart = new Cart(1);
        System.out.println(Cart.getTotalAmount(1));
    }

    public static boolean linearSearch(int[] array, int i) {
        for (int num : array)
            if (num == i)
                return true;
        return false;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(int userID) {
        user = new User(userID);
    }
}

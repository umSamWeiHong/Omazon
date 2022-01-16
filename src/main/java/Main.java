package main.java;

import main.java.gui.Page;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Omazon!");

        for (Page page : Page.values()) {
            String s = page.toString();
            System.out.println(s);
            System.out.println(page.getFilename());
        }

        Page page1 = Page.valueOf("LOGIN");
        System.out.println(page1);

        Page page = Page.valueOf("Login");
        System.out.println(page);


    }
}

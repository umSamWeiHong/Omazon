package main.java;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author User
 */

public class Login {
    
    private String username;
    private String email;
    private String password;
    
    /**Initialize variables*/
    public Login(String username,String email,String password) {
        this.username = "";
        this.email = "";
        this.password = "";
    }
    
    /**Enter user details and check its validity*/
    public void login(){
		
		Scanner sc = new Scanner(System.in);
                String userNAME = "SELECT Username FROM personal_information";
                String passWORD = "SELECT Password FROM personal_information";
                while(!((username.equals(userNAME))&&(password.equals(passWORD)))){
                    System.out.print("Username: ");
                    username = sc.next();
                    System.out.print("Password: ");
                    password = sc.next();
                    
                    try{
                        System.out.println("\nValidating...");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://139.99.88.50:3306/?user=omazonremote","omazonremote","f798SuFM");
                        Statement stmt = (Statement)conn.createStatement();
                        String query = "select * from user where username LIKE '"+username+"'COLLATE utf8mb4_bin AND password LIKE '"+password+"'COLLATE utf8mb4_bin";
                        ResultSet resultSet = stmt.executeQuery(query);
                        if(resultSet.next()){
                        
                            String userName = resultSet.getString("Username");
                            System.out.println("You have logged into successfully, user " + userName);
                            break;
                          
                        }
                    
                        else{ 
                            System.out.println("\nNo account has been found. Please try again.");    
                        }
                    }
              
                    catch (SQLException e) {
                        System.err.println(e);
                    }
                
               }
    }
    
    
    
    /**Create an account and store it in database*/         
    public void register(){
		
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
	username = sc.next();
        System.out.print("Email: ");
	email = sc.next();
	System.out.print("Password: ");
	password = sc.next();
	System.out.print("\nYou have registered successfully, user " + username);
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://139.99.88.50:3306/?user=omazonremote","omazonremote","f798SuFM");
            Statement stmt = (Statement)conn.createStatement();
            String insert = "INSERT INTO omazon.user(username,password,email)VALUES('"+username+"','"+password+"','"+email+"')";
            stmt.executeUpdate(insert);
                    
        } 
        catch (SQLException e) {
            System.err.println(e); 
        }
    }
}

    


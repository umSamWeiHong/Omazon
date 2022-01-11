/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main.java;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class Login extends StoredDB {
    private int userID;
    private String username,password,email;
    private Timestamp create_time;

    /** Create a Login object with data from database. */
    public Login(int userID) {
        String query = "select * from user where username LIKE '"+username+"'COLLATE utf8mb4_bin AND password LIKE '"+password+"'COLLATE utf8mb4_bin";
        ResultSet resultSet = Database.queryDatabase(query);
        if (resultSet == null)
            throw new IllegalArgumentException("userID is not found.");

        try {
            resultSet.next();
            userID = resultSet.getInt("userID");
            username = resultSet.getString("username");
            password = resultSet.getString("password");
            email = resultSet.getString("email");
            create_time = resultSet.getTimestamp("create_time");
            setInDatabase(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Login object with all parameters (except userID). */
    public Login(String username, String password, String email, Timestamp create_Time) {
        Scanner sc = new Scanner(System.in);
        String userNAME = sc.next();
        String eMAIL = sc.next();
        String passWORD = sc.next();
        if(!(userNAME.equals(username)&&eMAIL.equals(email)&&passWORD.equals(password))){
 
            this.username = username;
            this.password = password;
            this.email = email;
            this.create_time = create_time;
        }
        else{
            System.out.println("Account exists. Please try creating other account");
        }
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }
    
    /** Edit the user details. */
    public void editDetails(Timestamp create_time, String username, String password, String email) {
        this.create_time = create_time;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
     /** Return the N recent user details. */
    public static StoredDB[] getUserDetails(int userID, int N) {
        String query = String.format("SELECT userID FROM user " +
                        "WHERE userID = %d " +
                        "ORDER BY create_time DESC",
                        userID);
        if (N != -1)
            query += " LIMIT " + N;
        return Database.getDBObjects(query, Login.class, N);
    }

    
    /**Return user details from user*/
    public static StoredDB[] getUserDetails(int userID) {
        return getUserDetails(userID, -1);
    }

   
    @Override
    public String toString() {
        return "user{" +
                "userID =" + userID +
                ", username =" + username +
                ", password =" + password +
                ", email =" + email +
                ", create_time =" +create_time +
                ", inDatabase=" + inDatabase() +
                '}';
    }

    public static void main(String[] args) {

        Login toDelete = new Login(6);
        System.out.println(toDelete);
        Database.delete(toDelete);

        for (StoredDB lg : Login.getUserDetails(6)) {
            System.out.println(lg);
        }
    }

    public static String getPrimaryKey() {
        return "userID";
    }

    @Override
    public String insertQuery() {
        return String.format("INSERT INTO " +
                "user (userID, username, password, email, create_time) " +
                "VALUES (%d, '%s', '%s', '%s', '%s')",
                userID, username, password, email, create_time);
    }

    @Override
    public String updateQuery() {
        return String.format("UPDATE user " +
               "SET create_time = '%s', username = '%s', password = '%s', email = '%s' " +
               "WHERE userID = %d",
               create_time, username, password, email, userID);
    }

    @Override
    public String deleteQuery() {
        return "DELETE FROM user " +
               "WHERE userID = " + userID;
    }
}

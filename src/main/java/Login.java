package main.java;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public static boolean emailExists(String email) {
        String query = String.format("""
                                     SELECT userID FROM User
                                     WHERE email = '%s'""",
                                     email);
        return Database.queryDatabase(query) != null;
    }

    public static boolean validate(int userID, String password) {
        String query = String.format("""
                                     SELECT userID FROM User
                                     WHERE userID = '%s' AND password = '%s'""",
                                     userID, password);
        ResultSet resultSet = Database.queryDatabase(query);

        return resultSet != null;
    }

    /** Return the userID with the given email and password, return 0 if not found. */
    public static int validate(String email, String password) {
        String query = String.format("""
                                     SELECT userID FROM User
                                     WHERE email = '%s' AND password = '%s'""",
                                     email, password);
        ResultSet resultSet = Database.queryDatabase(query);

        if (resultSet != null)
            try {
                resultSet.next();
                return resultSet.getInt("userID");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return 0;
    }

    public static void addNewUser(String username, String email, String password) {
        System.out.println(new User(username, email, password));
        Database.add(new User(username, email, password));
    }
}

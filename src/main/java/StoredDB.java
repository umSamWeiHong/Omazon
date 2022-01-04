package main.java;

public interface StoredDB {

    boolean inDatabase();

    String insertQuery();
    String updateQuery();
    String deleteQuery();
}

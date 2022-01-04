package main.java;

public abstract class StoredDB {

    protected boolean inDatabase;

    public void setInDatabase(boolean inDatabase) {
        this.inDatabase = inDatabase;
    }

    public boolean inDatabase() {
        return inDatabase;
    }

    public abstract String insertQuery();
    public abstract String updateQuery();
    public abstract String deleteQuery();
}

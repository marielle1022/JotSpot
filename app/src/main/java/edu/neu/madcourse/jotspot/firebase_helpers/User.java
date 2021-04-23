package edu.neu.madcourse.jotspot.firebase_helpers;

public class User {

    private String username;
    private String hashedPassword;

    // Empty (default) constructor needed for calls to DataSnapshot.getValue
    public User() {}

    // Constructor with username and hashed password
    // TODO: is token needed?
    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    // Constructor with username
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

}

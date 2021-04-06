package edu.neu.madcourse.jotspot.firebase_helpers;

public class User {

    private String username;
    private String token;

    // Empty (default) constructor needed for calls to DataSnapshot.getValue
    public User() {}

    // Constructor with username and empty token
    // TODO: is this needed?
    public User(String username) {
        this.username = username;
        this.token="";
    }

    // Constructor with username and token
    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Username: " + this.username + " Token: " + this.token;
    }

}

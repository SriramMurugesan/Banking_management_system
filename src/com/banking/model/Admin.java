package com.banking.model;

/**
 * Admin model for bank staff authentication
 */
public class Admin {
    private int adminId;
    private String username;
    private String password;
    private String fullName;
    private String email;

    // Constructors
    public Admin() {
    }

    public Admin(int adminId, String username, String fullName, String email) {
        this.adminId = adminId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.blinkit.model;

public class User {

    private int id;
    private String mobile;
    private String email;
    private String name;
    private String password;
    private String role;

    // Constructor
    public User(
            int id,
            String mobile,
            String email,
            String name,
            String password,
            String role) {

        this.id = id;
        this.mobile = mobile;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Mobile
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Check Admin
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}
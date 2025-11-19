package com.offlineexam.model;

public class User {
    public enum Role {
        ADMIN, TEACHER, STUDENT
    }

    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Role role;

    public User(String userId, String username, String password, String fullName, String email, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Constructor for creating new users
    public User(String username, String password, String fullName, String email, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return (fullName == null || fullName.isEmpty()) ? username : fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return (email == null || email.isEmpty()) ? "N/A" : email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
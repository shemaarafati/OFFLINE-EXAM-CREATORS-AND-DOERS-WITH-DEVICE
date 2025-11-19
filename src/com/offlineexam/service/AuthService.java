package com.offlineexam.service;

import com.offlineexam.model.User;
import com.offlineexam.util.DBConnection;
import java.sql.*;

public class AuthService {

    public User login(String username, String password) {
        String uname = username == null ? "" : username.trim();
        String pwd = password == null ? "" : password.trim();
        String sql = "SELECT user_id, username, password, role FROM users WHERE LOWER(username) = LOWER(?) AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uname);
            stmt.setString(2, pwd);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String roleStr = rs.getString("role");
                User.Role role;
                try {
                    role = User.Role.valueOf(roleStr == null ? "STUDENT" : roleStr.trim().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    role = User.Role.STUDENT;
                }

                String unameDb = rs.getString("username");
                String pwdDb = rs.getString("password");

                User user = new User(
                        rs.getString("user_id"),
                        unameDb,
                        pwdDb,
                        unameDb, // fullName fallback
                        null,     // email not in schema
                        role
                );

                // Log successful login to userlogs
                LogService.addLog(user.getUserId(), "logged in as " + user.getRole());

                return user;
            } else {
                // Debug: check if username exists but password mismatched
                try (PreparedStatement checkUser = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE LOWER(username)=LOWER(?)")) {
                    checkUser.setString(1, uname);
                    try (ResultSet crs = checkUser.executeQuery()) {
                        if (crs.next() && crs.getInt(1) > 0) {
                            System.err.println("AuthService: Username found but password mismatch for '" + uname + "'.");
                        } else {
                            System.err.println("AuthService: Username not found: '" + uname + "'.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.offlineexam.service;

import com.offlineexam.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.offlineexam.util.DBConnection;

public class UserService {

    public static boolean createUser(User user) {
        System.out.println("UserService: Creating user: " + user.getUsername());

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());

            int affectedRows = stmt.executeUpdate();
            System.out.println("User creation - Affected rows: " + affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateUserProfile(String userId, String username, String password, String fullName, String email) {
        System.out.println("UserService: Updating user profile ID: " + userId);

        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();

        boolean first = true;
        if (username != null && !username.isEmpty()) {
            sql.append(first ? "username = ?" : ", username = ?");
            params.add(username);
            first = false;
        }
        if (password != null && !password.isEmpty()) {
            sql.append(first ? "password = ?" : ", password = ?");
            params.add(password);
            first = false;
        }

        if (first) {
            System.out.println("No fields to update for user: " + userId);
            return true; // nothing to update
        }

        sql.append(" WHERE user_id = ?");
        params.add(userId);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            int affectedRows = stmt.executeUpdate();
            System.out.println("User update - Affected rows: " + affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteUser(String userId) {
        System.out.println("UserService: Deleting user ID: " + userId);

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            int affectedRows = stmt.executeUpdate();
            System.out.println("User deletion - Affected rows: " + affectedRows);

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static List<User> getAllUsers() {
        System.out.println("UserService: Getting all users");
        List<User> users = new ArrayList<>();

        String sql = "SELECT user_id, username, password, role FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String roleStr = rs.getString("role");

                User.Role role = User.Role.valueOf(roleStr);
                User user = new User(id, username, password, username, null, role);
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public static User getUserById(String userId) {
        System.out.println("UserService: Getting user by ID: " + userId);

        String sql = "SELECT user_id, username, password, role FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("user_id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String roleStr = rs.getString("role");
                    User.Role role = User.Role.valueOf(roleStr);
                    return new User(id, username, password, username, null, role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static List<User> getUsersByRole(User.Role role) {
        System.out.println("UserService: Getting users by role: " + role);
        List<User> users = new ArrayList<>();

        String sql = "SELECT user_id, username, password FROM users WHERE role = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                User user = new User(id, username, password, username, null, role);
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Error getting users by role: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public static User authenticateUser(String username, String password) {
        System.out.println("UserService: Authenticating user: " + username);

        String sql = "SELECT user_id, username, password, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("user_id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                String roleStr = rs.getString("role");

                User.Role role = User.Role.valueOf(roleStr);
                User user = new User(id, dbUsername, dbPassword, dbUsername, null, role);

                System.out.println("User authenticated successfully: " + dbUsername);
                return user;
            }

        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
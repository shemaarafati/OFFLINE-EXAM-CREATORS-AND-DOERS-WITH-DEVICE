package com.offlineexam.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/offline_exam_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Failed to load MySQL JDBC Driver: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Attempting database connection to: " + URL);
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Database connection established successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    // Test connection
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}

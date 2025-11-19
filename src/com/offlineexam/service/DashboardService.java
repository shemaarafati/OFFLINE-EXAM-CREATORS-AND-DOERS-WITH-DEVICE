package com.offlineexam.service;

import com.offlineexam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {

    private static int count(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("DashboardService count error for sql: " + sql + " -> " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalCourses() {
        return count("SELECT COUNT(*) FROM courses");
    }

    public static int getTotalStudents() {
        return count("SELECT COUNT(*) FROM users WHERE role = 'STUDENT'");
    }

    public static int getTotalTeachers() {
        return count("SELECT COUNT(*) FROM users WHERE role = 'TEACHER'");
    }

    public static int getTotalUsers() {
        return count("SELECT COUNT(*) FROM users");
    }

    public static int getTotalExams() {
        return count("SELECT COUNT(*) FROM exams");
    }

    public static int getPendingRequests() {
        return count("SELECT COUNT(*) FROM enrollments WHERE status = 'PENDING'");
    }
}

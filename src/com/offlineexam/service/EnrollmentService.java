package com.offlineexam.service;

import com.offlineexam.model.Enrollment;
import com.offlineexam.model.EnrollmentRequest;
import com.offlineexam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentService {

    // Create a new enrollment request (used by StudentDashboard)
    public static boolean requestEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.setString(3, enrollment.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Pending enrollments for a teacher's courses (used by TeacherDashboard)
    public static List<Enrollment> getPendingEnrollmentsByTeacher(String teacherId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.* FROM enrollments e " +
                "JOIN courses c ON e.course_id = c.course_id " +
                "WHERE c.created_by = ? AND e.status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getString("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date").toLocalDateTime(),
                        rs.getString("approved_by")
                );
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    // Update enrollment status (APPROVED / REJECTED) (used by TeacherDashboard/AdminDashboard)
    public static boolean updateEnrollmentStatus(int enrollmentId, String status, String approvedBy) {
        String sql = "UPDATE enrollments SET status = ?, approved_by = ? WHERE enrollment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, approvedBy);
            stmt.setInt(3, enrollmentId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Approved enrollments for a student (used by StudentDashboard to get their courses)
    public static List<Enrollment> getStudentEnrollments(String studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND status = 'APPROVED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getString("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date").toLocalDateTime(),
                        rs.getString("approved_by")
                );
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    // Count approved enrollments for a course (used in dashboard stats)
    public static int countApprovedEnrollmentsByCourse(int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ? AND status = 'APPROVED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get latest enrollment status for a specific student and course (PENDING/APPROVED/REJECTED) or null if none
    public static String getEnrollmentStatus(String studentId, int courseId) {
        String sql = "SELECT status FROM enrollments WHERE student_id = ? AND course_id = ? " +
                "ORDER BY request_date DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            stmt.setInt(2, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all PENDING enrollments (used by AdminDashboard)
    public static List<Enrollment> getAllPendingEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE status = 'PENDING' ORDER BY request_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getString("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date").toLocalDateTime(),
                        rs.getString("approved_by")
                );
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    // Helper to create an enrollment request object from EnrollmentRequest model
    public static boolean createEnrollmentRequest(EnrollmentRequest request) {
        Enrollment enrollment = new Enrollment(
                request.getStudent(),
                Integer.parseInt(request.getCourse()) // Convert course ID to int
        );
        enrollment.setRequestDate(LocalDateTime.now());
        return requestEnrollment(enrollment);
    }

    public static int countApprovedEnrollmentsByCourse(String courseId) {
        return countApprovedEnrollmentsByCourse(Integer.parseInt(courseId));
    }

    // Additional utility methods
    public static List<Enrollment> getEnrollmentsByStudent(String studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getString("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date").toLocalDateTime(),
                        rs.getString("approved_by")
                );
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public static List<Enrollment> getEnrollmentsByCourse(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getString("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date").toLocalDateTime(),
                        rs.getString("approved_by")
                );
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public static Enrollment getEnrollmentById(int enrollmentId) {
        String sql = "SELECT * FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enrollmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Enrollment(
                            rs.getInt("enrollment_id"),
                            rs.getString("student_id"),
                            rs.getInt("course_id"),
                            rs.getString("status"),
                            rs.getTimestamp("request_date").toLocalDateTime(),
                            rs.getString("approved_by")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get all enrollments (for admin purposes)
    public static List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getString("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date").toLocalDateTime(),
                        rs.getString("approved_by")
                );
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }
}
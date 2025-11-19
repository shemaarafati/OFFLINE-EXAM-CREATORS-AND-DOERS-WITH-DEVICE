package com.offlineexam.service;

import com.offlineexam.model.Course;
import com.offlineexam.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseService {

    public static List<Course> getCoursesByTeacher(String teacherId) {
        System.out.println("CourseService: Loading courses for teacher ID: " + teacherId);
        List<Course> courses = new ArrayList<>();

        String sql = "SELECT course_id, course_name, description, created_by FROM courses WHERE created_by = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                String description = rs.getString("description");
                String createdBy = rs.getString("created_by");
                Course course = new Course(courseId, courseName, null, description);
                course.setCreatedBy(createdBy);
                courses.add(course);
            }

        } catch (SQLException e) {
            System.err.println("Error loading courses for teacher: " + e.getMessage());
        }

        return courses;
    }

    // Add missing methods
    public static List<Course> getAllCourses() {
        System.out.println("CourseService: Getting all courses");
        List<Course> courses = new ArrayList<>();

        String sql = "SELECT c.course_id, c.course_name, c.description, c.created_by FROM courses c";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                String description = rs.getString("description");
                String createdBy = rs.getString("created_by");
                Course course = new Course(courseId, courseName, null, description);
                course.setCreatedBy(createdBy);
                courses.add(course);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all courses: " + e.getMessage());
            e.printStackTrace();

            // Fallback: return basic course list
            sql = "SELECT course_id, course_name, description, created_by FROM courses";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int courseId = rs.getInt("course_id");
                    String courseName = rs.getString("course_name");
                    String description = rs.getString("description");
                    String createdBy = rs.getString("created_by");
                    Course course = new Course(courseId, courseName, null, description);
                    course.setCreatedBy(createdBy);
                    courses.add(course);
                }

            } catch (SQLException e2) {
                System.err.println("Error in fallback query: " + e2.getMessage());
            }
        }

        return courses;
    }

    public static Course getCourseById(int courseId) {
        System.out.println("CourseService: Getting course by ID: " + courseId);

        String sql = "SELECT course_id, course_name, description FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String courseName = rs.getString("course_name");
                String description = rs.getString("description");

                return new Course(courseId, courseName, null, description);
            }

        } catch (SQLException e) {
            System.err.println("Error getting course by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static boolean createCourse(Course course) {
        System.out.println("CourseService: Creating course: " + course.getCourseName());

        String sql = "INSERT INTO courses (course_name, description) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getDescription());

            int affectedRows = stmt.executeUpdate();
            System.out.println("Course creation - Affected rows: " + affectedRows);

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        course.setCourseId(generatedId);
                        System.out.println("Generated course ID: " + generatedId);
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating course: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean assignCourseTeacher(String courseId, String teacherId) {
        System.out.println("CourseService: Assigning teacher " + teacherId + " to course ID: " + courseId);

        String sql = "UPDATE courses SET created_by = ? WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (teacherId == null || teacherId.trim().isEmpty()) {
                stmt.setNull(1, Types.VARCHAR);
            } else {
                stmt.setString(1, teacherId.trim());
            }
            stmt.setInt(2, Integer.parseInt(courseId));

            int affectedRows = stmt.executeUpdate();
            System.out.println("Course teacher assignment - Affected rows: " + affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error assigning teacher to course: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid course ID format: " + courseId);
        }

        return false;
    }

    public static boolean updateCourse(String courseId, String courseName, String courseCode, String description) {
        System.out.println("CourseService: Updating course ID: " + courseId);

        String sql = "UPDATE courses SET course_name = ?, description = ? WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseName);
            stmt.setString(2, description);
            stmt.setInt(3, Integer.parseInt(courseId));

            int affectedRows = stmt.executeUpdate();
            System.out.println("Course update - Affected rows: " + affectedRows);

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid course ID format: " + courseId);
        }

        return false;
    }

    public static boolean deleteCourse(String courseId) {
        System.out.println("CourseService: Deleting course ID: " + courseId);

        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(courseId));

            int affectedRows = stmt.executeUpdate();
            System.out.println("Course deletion - Affected rows: " + affectedRows);

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid course ID format: " + courseId);
        }

        return false;
    }
}
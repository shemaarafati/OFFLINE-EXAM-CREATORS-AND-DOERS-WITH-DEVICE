package com.offlineexam.model;

public class Course {
    private int courseId;
    private String courseName;
    private String courseCode;
    private String description;
    private String createdBy;
    private String teacherName; // For display purposes

    // Removed legacy constructor to avoid conflict with (int, String, String, String)

    // createdBy is typically set via DB; omit (String,String,String createdBy) constructor to avoid signature conflict

    // Constructor used by views when only name/code/description are provided
    public Course(String courseName, String courseCode, String description) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
    }

    // Additional constructors used by services/views
    public Course(int courseId, String courseName, String courseCode, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
    }

    public Course(int courseId, String courseName, String courseCode, String description, String teacherName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
        this.teacherName = teacherName;
    }

    // Getters and setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    // Alias expected by some views
    public String getTeacher() { return teacherName; }

    @Override
    public String toString() {
        return courseName;
    }
}
package com.offlineexam.model;

import java.time.LocalDateTime;

public class Enrollment {
    private int enrollmentId;
    private String studentId;
    private int courseId;
    private String status;
    private LocalDateTime requestDate;
    private String approvedBy;

    public Enrollment(int enrollmentId, String studentId, int courseId, String status,
                      LocalDateTime requestDate, String approvedBy) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
        this.requestDate = requestDate;
        this.approvedBy = approvedBy;
    }

    // Constructor for new enrollment requests
    public Enrollment(String studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = "PENDING";
    }

    // Getters and setters
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
}
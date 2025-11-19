package com.offlineexam.model;

import java.time.LocalDate;

public class Exam {
    private int examId;
    private int courseId;
    private String examName;
    private LocalDate examDate;
    private int durationMinutes;
    private String createdBy;
    private int totalMarks;

    public Exam(int examId, int courseId, String examName, LocalDate examDate,
                int durationMinutes, int totalMarks, String createdBy) {
        this.examId = examId;
        this.courseId = courseId;
        this.examName = examName;
        this.examDate = examDate;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.createdBy = createdBy;
    }

    // Constructor for new exams
    public Exam(int courseId, String examName, LocalDate examDate,
                int durationMinutes, int totalMarks, String createdBy) {
        this.courseId = courseId;
        this.examName = examName;
        this.examDate = examDate;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.createdBy = createdBy;
    }

    // Getters and setters
    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
}
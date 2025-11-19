package com.offlineexam.model.dto;

import java.time.LocalDate;

public class ExamDTO {
    private int courseId;
    private String examName;
    private LocalDate examDate;
    private int durationMinutes;
    private int totalMarks;
    private String createdBy;

    // Default constructor
    public ExamDTO() {}

    // Parameterized constructor
    public ExamDTO(int courseId, String examName, LocalDate examDate, int durationMinutes, int totalMarks, String createdBy) {
        this.courseId = courseId;
        this.examName = examName;
        this.examDate = examDate;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    @Override
    public String toString() {
        return "ExamDTO{" +
                "courseId=" + courseId +
                ", examName='" + examName + '\'' +
                ", examDate=" + examDate +
                ", durationMinutes=" + durationMinutes +
                ", totalMarks=" + totalMarks +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}

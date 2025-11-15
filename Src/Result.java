package com.offlineexam.model;

import java.time.LocalDateTime;

public class Result {
    private int resultId;
    private String studentId;
    private int examId;
    private double score;
    private String gradedBy;
    private LocalDateTime dateTaken;

    public Result(int resultId, String studentId, int examId, double score,
                  String gradedBy, LocalDateTime dateTaken) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.examId = examId;
        this.score = score;
        this.gradedBy = gradedBy;
        this.dateTaken = dateTaken;
    }

    // Constructor for new results
    public Result(String studentId, int examId, double score, String gradedBy) {
        this.studentId = studentId;
        this.examId = examId;
        this.score = score;
        this.gradedBy = gradedBy;
    }

    // Getters and setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getGradedBy() { return gradedBy; }
    public void setGradedBy(String gradedBy) { this.gradedBy = gradedBy; }

    public LocalDateTime getDateTaken() { return dateTaken; }
    public void setDateTaken(LocalDateTime dateTaken) { this.dateTaken = dateTaken; }
}

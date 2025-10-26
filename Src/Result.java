package com.offlineexam.model;

import java.io.Serializable;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int studentId;
    private int examId;
    private double score;
    public Result() {} public Result(int id,int studentId,int examId,double score){ this.id=id; this.studentId=studentId; this.examId=examId; this.score=score; }
    public int getId(){ return id; } public int getStudentId(){ return studentId; } public int getExamId(){ return examId; } public double getScore(){ return score; }
    public void setScore(double s){ this.score = s; }
    @Override public String toString(){ return id + " - Stu:"+studentId+" Exam:"+examId+" Score:"+score; }
}

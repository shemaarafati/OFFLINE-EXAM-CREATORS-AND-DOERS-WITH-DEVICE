package com.offlineexam.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private int courseId;
    private int durationMinutes;
    private List<Question> questions = new ArrayList<>();

    public Exam() {}

    public Exam(int id, String title, int courseId, int durationMinutes){
        this.id=id;
        this.title=title;
        this.courseId=courseId;
        this.durationMinutes=durationMinutes;
    }

    public int getId(){return id;}
    public void setId(int id) { this.id = id; } // ADD THIS METHOD

    public String getTitle(){return title;}
    public int getCourseId(){return courseId;}
    public int getDurationMinutes(){return durationMinutes;}
    public List<Question> getQuestions(){return questions;}
    public void addQuestion(Question q){ questions.add(q); }

    @Override
    public String toString(){
        return id + " - " + title + " (Course:"+courseId+")";
    }
}
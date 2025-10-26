package com.offlineexam.model;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private User teacher; // assigned teacher
    private Integer teacherId; // for easier reference

    public Course() {}

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) {
        this.teacher = teacher;
        this.teacherId = teacher != null ? teacher.getId() : null;
    }

    public Integer getTeacherId() { return teacherId; }
    public void setTeacherId(Integer teacherId) { this.teacherId = teacherId; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + description + ")" +
                (teacher != null ? " - Teacher: " + teacher.getUsername() : "");
    }
}
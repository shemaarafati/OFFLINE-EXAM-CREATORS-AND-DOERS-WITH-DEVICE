package com.offlineexam.model;

import java.io.Serializable;
import java.util.Objects;

public class EnrollmentRequest implements Serializable {
    private String student;
    private String course;
    private String teacher;
    private String status; // Pending, Approved

    public EnrollmentRequest(String student, String course, String teacher) {
        this.student = student;
        this.course = course;
        this.teacher = teacher;
        this.status = "Pending";
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentRequest)) return false;
        EnrollmentRequest that = (EnrollmentRequest) o;
        return Objects.equals(student, that.student) &&
                Objects.equals(course, that.course) &&
                Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course, teacher);
    }
}

package com.offlineexam.service;

import com.offlineexam.model.Course;
import com.offlineexam.model.User;
import com.offlineexam.util.DataStore;

import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private static final String COURSE_FILE = "courses.dat";
    private static List<Course> courses = DataStore.load(COURSE_FILE);
    private static int idSeq = 1;

    static {
        for (Course c : courses) {
            if (c.getId() >= idSeq) idSeq = c.getId() + 1;
        }
    }

    public static List<Course> listAll() {
        return new ArrayList<>(courses);
    }

    public static void createCourse(Course c) {
        c.setId(idSeq++);
        courses.add(c);
        DataStore.save(COURSE_FILE, courses);
    }

    public static void assignTeacher(int courseId, int teacherId) {
        User teacher = UserService.findById(teacherId);

        if (teacher == null || !"teacher".equals(teacher.getRole())) {
            return;
        }

        for (Course c : courses) {
            if (c.getId() == courseId) {
                c.setTeacher(teacher);
                c.setTeacherId(teacherId);
            }
        }
        DataStore.save(COURSE_FILE, courses);
    }

    public static Course findById(int id) {
        for (Course c : courses) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public static List<Course> findByTeacherId(int teacherId) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (c.getTeacherId() != null && c.getTeacherId() == teacherId) {
                result.add(c);
            }
        }
        return result;
    }
}
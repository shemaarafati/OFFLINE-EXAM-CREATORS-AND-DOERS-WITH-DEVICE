package com.offlineexam.controller;

import com.offlineexam.model.Exam;
import com.offlineexam.service.ExamService;
import java.util.List;

public class ExamController {

    public static boolean createExam(Exam exam) {
        return ExamService.createExamWithTotalMarks(exam);
    }

    public static List<Exam> getExamsByTeacher(String teacherId) {
        return ExamService.getExamsByTeacher(teacherId);
    }

    public static Exam getExamById(int examId) {
        return ExamService.getExamById(examId);
    }

    public static boolean updateExam(Exam exam) {
        // You'll need to implement this method in ExamService
        // For now, return false
        return false;
    }

    public static boolean deleteExam(int examId) {
        // You'll need to implement this method in ExamService
        // For now, return false
        return false;
    }
}
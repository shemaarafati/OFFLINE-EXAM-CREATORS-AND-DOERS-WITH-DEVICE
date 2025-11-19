package com.offlineexam.service;

import com.offlineexam.model.Exam;
import com.offlineexam.model.Question;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.offlineexam.util.DBConnection;

public class ExamService {
    

    public static boolean createExamWithTotalMarks(Exam exam) {
        // Your exams table does not have a total_marks column.
        // Use the existing createExam method which matches the schema
        // (exam_id, course_id, exam_name, exam_date, duration_minutes, created_by).
        System.out.println("ExamService: createExamWithTotalMarks -> delegating to createExam (no total_marks column in DB)");
        return createExam(exam);
    }

    // Add missing methods
    public static List<Exam> getExamsByTeacher(String teacherId) {
        System.out.println("ExamService: Getting exams for teacher ID: " + teacherId);
        List<Exam> exams = new ArrayList<>();

        String sql = "SELECT exam_id, course_id, exam_name, exam_date, duration_minutes, created_by " +
                "FROM exams WHERE created_by = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int examId = rs.getInt("exam_id");
                int courseId = rs.getInt("course_id");
                String examName = rs.getString("exam_name");
                LocalDate examDate = rs.getDate("exam_date").toLocalDate();
                int durationMinutes = rs.getInt("duration_minutes");
                String createdBy = rs.getString("created_by");

                // totalMarks is not stored directly in exams table; compute lazily when needed
                Exam exam = new Exam(examId, courseId, examName, examDate, durationMinutes, 0, createdBy);
                exams.add(exam);
            }

        } catch (SQLException e) {
            System.err.println("Error getting exams by teacher: " + e.getMessage());
            e.printStackTrace();
        }

        return exams;
    }

    public static Exam getExamById(int examId) {
        System.out.println("ExamService: Getting exam by ID: " + examId);

        String sql = "SELECT exam_id, course_id, exam_name, exam_date, duration_minutes, created_by " +
                "FROM exams WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int courseId = rs.getInt("course_id");
                String examName = rs.getString("exam_name");
                LocalDate examDate = rs.getDate("exam_date").toLocalDate();
                int durationMinutes = rs.getInt("duration_minutes");
                String createdBy = rs.getString("created_by");

                return new Exam(examId, courseId, examName, examDate, durationMinutes, 0, createdBy);
            }

        } catch (SQLException e) {
            System.err.println("Error getting exam by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Get all exams for a given course
    public static List<Exam> getExamsByCourse(int courseId) {
        System.out.println("ExamService: Getting exams for course ID: " + courseId);
        List<Exam> exams = new ArrayList<>();

        String sql = "SELECT exam_id, course_id, exam_name, exam_date, duration_minutes, created_by " +
                "FROM exams WHERE course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int examId = rs.getInt("exam_id");
                int courseIdDb = rs.getInt("course_id");
                String examName = rs.getString("exam_name");
                LocalDate examDate = rs.getDate("exam_date").toLocalDate();
                int durationMinutes = rs.getInt("duration_minutes");
                String createdBy = rs.getString("created_by");

                exams.add(new Exam(examId, courseIdDb, examName, examDate, durationMinutes, 0, createdBy));
            }

        } catch (SQLException e) {
            System.err.println("Error getting exams by course: " + e.getMessage());
            e.printStackTrace();
        }

        return exams;
    }

    // UPDATE exam (title/date/duration)
    public static boolean updateExam(Exam exam) {
        System.out.println("ExamService: Updating exam ID: " + exam.getExamId());

        String sql = "UPDATE exams SET exam_name = ?, exam_date = ?, duration_minutes = ? " +
                     "WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exam.getExamName());
            stmt.setDate(2, Date.valueOf(exam.getExamDate()));
            stmt.setInt(3, exam.getDurationMinutes());
            stmt.setInt(4, exam.getExamId());

            int affected = stmt.executeUpdate();
            System.out.println("ExamService.updateExam affected rows: " + affected);
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating exam: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // DELETE exam and its questions
    public static boolean deleteExam(int examId) {
        System.out.println("ExamService: Deleting exam ID: " + examId);

        String deleteQuestionsSql = "DELETE FROM questions WHERE exam_id = ?";
        String deleteExamSql = "DELETE FROM exams WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteQuestionsStmt = conn.prepareStatement(deleteQuestionsSql);
                 PreparedStatement deleteExamStmt = conn.prepareStatement(deleteExamSql)) {

                deleteQuestionsStmt.setInt(1, examId);
                int qRows = deleteQuestionsStmt.executeUpdate();
                System.out.println("Deleted " + qRows + " questions for exam ID " + examId);

                deleteExamStmt.setInt(1, examId);
                int eRows = deleteExamStmt.executeUpdate();
                System.out.println("Deleted exams rows: " + eRows);

                conn.commit();
                return eRows > 0;
            } catch (SQLException inner) {
                conn.rollback();
                System.err.println("Error deleting exam: " + inner.getMessage());
                inner.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error in deleteExam transaction: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Add the missing methods that were causing errors
    public static int getQuestionCount(int examId) {
        System.out.println("ExamService: Getting question count for exam ID: " + examId);

        String sql = "SELECT COUNT(*) as question_count FROM questions WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("question_count");
            }

        } catch (SQLException e) {
            System.err.println("Error getting question count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static int getTotalMarks(int examId) {
        System.out.println("ExamService: Getting total marks for exam ID: " + examId + " (derived from questions table)");
        // exams table has no total_marks column; always calculate from questions
        return calculateTotalMarksFromQuestions(examId);
    }

    private static int calculateTotalMarksFromQuestions(int examId) {
        String sql = "SELECT SUM(marks) as total_marks FROM questions WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total_marks");
            }

        } catch (SQLException e) {
            System.err.println("Error calculating total marks from questions: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static boolean addQuestion(Question question) {
        System.out.println("ExamService: Adding question to exam ID: " + question.getExamId());

        // Persist full question including options and correct answer
        // Expected columns in questions table:
        // question_id (PK), exam_id, question_text, question_type,
        // option_a, option_b, option_c, option_d, correct_answer, marks
        String sql = "INSERT INTO questions (exam_id, question_text, question_type, " +
                "option_a, option_b, option_c, option_d, correct_answer, marks) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, question.getExamId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getQuestionType());
            stmt.setString(4, question.getOptionA());
            stmt.setString(5, question.getOptionB());
            stmt.setString(6, question.getOptionC());
            stmt.setString(7, question.getOptionD());
            stmt.setString(8, question.getCorrectAnswer());
            stmt.setInt(9, question.getMarks());

            int affectedRows = stmt.executeUpdate();
            System.out.println("Question added - Affected rows: " + affectedRows);

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int questionId = generatedKeys.getInt(1);
                        question.setQuestionId(questionId);
                        System.out.println("Generated question ID: " + questionId);
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error adding question: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static List<Question> getQuestionsByExam(int examId) {
        System.out.println("ExamService: Getting questions for exam ID: " + examId);
        List<Question> questions = new ArrayList<>();

        String sql = "SELECT question_id, exam_id, question_text, question_type, " +
                "option_a, option_b, option_c, option_d, correct_answer, marks " +
                "FROM questions WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int questionId = rs.getInt("question_id");
                int examIdFromDb = rs.getInt("exam_id");
                String questionText = rs.getString("question_text");
                String questionType = rs.getString("question_type");
                String optionA = rs.getString("option_a");
                String optionB = rs.getString("option_b");
                String optionC = rs.getString("option_c");
                String optionD = rs.getString("option_d");
                String correctAnswer = rs.getString("correct_answer");
                int marks = rs.getInt("marks");

                Question question = new Question(
                        questionId,
                        examIdFromDb,
                        questionText,
                        optionA,
                        optionB,
                        optionC,
                        optionD,
                        correctAnswer,
                        marks
                );

                // Preserve explicit question type from DB (MCQ / TRUE_FALSE / ESSAY)
                if (questionType != null && !questionType.isEmpty()) {
                    question.setQuestionType(questionType);
                }
                questions.add(question);
            }

        } catch (SQLException e) {
            System.err.println("Error getting questions by exam: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }

    // Get all exams in the system (used for backup/sync)
    public static List<Exam> getAllExams() {
        System.out.println("ExamService: Getting all exams for backup/sync");
        List<Exam> exams = new ArrayList<>();

        String sql = "SELECT exam_id, course_id, exam_name, exam_date, duration_minutes, created_by FROM exams";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int examId = rs.getInt("exam_id");
                int courseId = rs.getInt("course_id");
                String examName = rs.getString("exam_name");
                LocalDate examDate = rs.getDate("exam_date").toLocalDate();
                int durationMinutes = rs.getInt("duration_minutes");
                String createdBy = rs.getString("created_by");

                exams.add(new Exam(examId, courseId, examName, examDate, durationMinutes, 0, createdBy));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all exams: " + e.getMessage());
            e.printStackTrace();
        }

        return exams;
    }

    // Alternative method if the main one doesn't work
    public static boolean createExam(Exam exam) {
        System.out.println("ExamService: Creating exam (alternative method)...");

        String sql = "INSERT INTO exams (course_id, exam_name, exam_date, duration_minutes, created_by) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, exam.getCourseId());
            stmt.setString(2, exam.getExamName());
            stmt.setDate(3, Date.valueOf(exam.getExamDate()));
            stmt.setInt(4, exam.getDurationMinutes());
            stmt.setString(5, exam.getCreatedBy());

            int affectedRows = stmt.executeUpdate();
            System.out.println("Alternative method - Affected rows: " + affectedRows);

            if (affectedRows > 0) {
                // Read generated exam_id so subsequent question inserts have a valid foreign key
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int examId = generatedKeys.getInt(1);
                        exam.setExamId(examId);
                        System.out.println("Alternative method - generated exam ID: " + examId);
                    }
                }
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in alternative method: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
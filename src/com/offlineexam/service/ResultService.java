package com.offlineexam.service;

import com.offlineexam.model.Result;
import com.offlineexam.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResultService {
    // Small in-memory cache as a fallback if DB queries fail
    private static final List<Result> cache = new ArrayList<>();

    public static void addResult(Result result) {
        // First try to persist in DB
        String sql = "INSERT INTO results (student_id, exam_id, score, graded_by, date_taken) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, result.getStudentId());
            stmt.setInt(2, result.getExamId());
            stmt.setDouble(3, result.getScore());
            stmt.setString(4, result.getGradedBy());

            LocalDateTime dt = result.getDateTaken();
            if (dt == null) {
                dt = LocalDateTime.now();
                result.setDateTaken(dt);
            }
            stmt.setTimestamp(5, Timestamp.valueOf(dt));

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        result.setResultId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ResultService: DB error on addResult: " + e.getMessage());
            e.printStackTrace();
        }

        // Always add to in-memory cache as well
        synchronized (cache) {
            cache.add(result);
            if (cache.size() > 500) {
                cache.remove(0);
            }
        }
    }

    public static List<Result> listAll() {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT result_id, student_id, exam_id, score, graded_by, date_taken FROM results";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
            return results;

        } catch (SQLException e) {
            System.err.println("ResultService: DB error on listAll, using cache: " + e.getMessage());
            e.printStackTrace();
        }

        synchronized (cache) {
            return new ArrayList<>(cache);
        }
    }

    public static List<Result> listByStudent(String studentId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT result_id, student_id, exam_id, score, graded_by, date_taken FROM results WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
            return results;

        } catch (SQLException e) {
            System.err.println("ResultService: DB error on listByStudent, using cache: " + e.getMessage());
            e.printStackTrace();
        }

        synchronized (cache) {
            List<Result> fallback = new ArrayList<>();
            for (Result r : cache) {
                if (studentId.equals(r.getStudentId())) {
                    fallback.add(r);
                }
            }
            return fallback;
        }
    }

    // Get all results for a specific exam
    public static List<Result> listByExam(int examId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT result_id, student_id, exam_id, score, graded_by, date_taken FROM results WHERE exam_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
            return results;

        } catch (SQLException e) {
            System.err.println("ResultService: DB error on listByExam, using cache: " + e.getMessage());
            e.printStackTrace();
        }

        synchronized (cache) {
            List<Result> fallback = new ArrayList<>();
            for (Result r : cache) {
                if (r.getExamId() == examId) {
                    fallback.add(r);
                }
            }
            return fallback;
        }
    }

    // Get a single result for a student and exam (latest match)
    public static Result findByStudentAndExam(String studentId, int examId) {
        String sql = "SELECT result_id, student_id, exam_id, score, graded_by, date_taken " +
                "FROM results WHERE student_id = ? AND exam_id = ? ORDER BY date_taken DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            stmt.setInt(2, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("ResultService: DB error on findByStudentAndExam, using cache: " + e.getMessage());
            e.printStackTrace();
        }

        synchronized (cache) {
            Result latest = null;
            for (Result r : cache) {
                if (studentId.equals(r.getStudentId()) && r.getExamId() == examId) {
                    latest = r;
                }
            }
            return latest;
        }
    }

    private static Result mapRow(ResultSet rs) throws SQLException {
        int resultId = rs.getInt("result_id");
        String studentId = rs.getString("student_id");
        int examId = rs.getInt("exam_id");
        double score = rs.getDouble("score");
        String gradedBy = rs.getString("graded_by");
        Timestamp ts = rs.getTimestamp("date_taken");
        LocalDateTime dateTaken = ts != null ? ts.toLocalDateTime() : null;

        Result result = new Result(resultId, studentId, examId, score, gradedBy, dateTaken);
        return result;
    }
}

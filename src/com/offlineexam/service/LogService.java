package com.offlineexam.service;

import com.offlineexam.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogService {
    // In-memory cache (used as a fallback and for quick access)
    private static final List<String> LOGS = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Existing API: add a log message without an explicit user id.
     * We store it in the DB with a null user_id and also keep it in memory.
     */
    public static synchronized void addLog(String event) {
        addLog(null, event);
    }

    /**
     * New API: add a log with explicit user_id and action text.
     * This writes to the userlogs table and updates the in-memory cache.
     */
    public static synchronized void addLog(String userId, String action) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String displayUser = (userId != null && !userId.isEmpty()) ? (userId + " - ") : "";
        String line = "[" + timestamp + "] " + displayUser + action;

        // Update in-memory cache
        LOGS.add(line);
        if (LOGS.size() > 500) {
            LOGS.remove(0);
        }

        // Persist to DB (user_logs table: log_id, user_id, action, log_time)
        String sql = "INSERT INTO user_logs (user_id, action, log_time) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (userId == null || userId.trim().isEmpty()) {
                stmt.setNull(1, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(1, userId.trim());
            }
            stmt.setString(2, action);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("LogService: Failed to persist log to DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns all in-memory logs (not primarily used by UI).
     */
    public static synchronized List<String> getLogs() {
        return new ArrayList<>(LOGS);
    }

    /**
     * Load recent logs from the userlogs table, ordered by time desc.
     * Falls back to the in-memory cache if the DB query fails.
     */
    public static synchronized List<String> getRecentLogs(int max) {
        if (max <= 0) {
            return Collections.emptyList();
        }

        List<String> recent = new ArrayList<>();
        String sql = "SELECT user_id, action, log_time FROM user_logs ORDER BY log_time DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, max);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String userId = rs.getString("user_id");
                    String action = rs.getString("action");
                    Timestamp ts = rs.getTimestamp("log_time");
                    String tsStr = ts != null ? ts.toLocalDateTime().format(FORMATTER) : "";

                    String displayUser = (userId != null && !userId.isEmpty()) ? (userId + " - ") : "";
                    String line = "[" + tsStr + "] " + displayUser + action;
                    recent.add(line);
                }
            }

            if (!recent.isEmpty()) {
                return recent;
            }

        } catch (SQLException e) {
            System.err.println("LogService: DB error on getRecentLogs, using in-memory cache: " + e.getMessage());
            e.printStackTrace();
        }

        // Fallback to in-memory cache
        if (LOGS.isEmpty()) {
            return Collections.emptyList();
        }
        int fromIndex = Math.max(0, LOGS.size() - max);
        return new ArrayList<>(LOGS.subList(fromIndex, LOGS.size()));
    }
}

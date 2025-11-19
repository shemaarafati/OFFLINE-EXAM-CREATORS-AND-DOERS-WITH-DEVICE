package com.offlineexam.service;

import com.offlineexam.model.Course;
import com.offlineexam.model.Enrollment;
import com.offlineexam.model.Exam;
import com.offlineexam.model.Result;
import com.offlineexam.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Sync and backup utilities.
 *
 * Right now this class:
 * - Collects data from local services
 * - Writes a JSON-like backup file to disk (for future upload to cloud)
 * - Provides stubs for sending data to a central server / Google Drive
 */
public class SyncService {

    private static final DateTimeFormatter FILE_TS = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final String BACKEND_BASE_URL = "https://your-backend.example.com";
    private static final String DRIVE_FOLDER_ID_PLACEHOLDER = "GOOGLE_DRIVE_FOLDER_ID_HERE";
    private static final String API_KEY = "CHANGE_ME_OFFLINE_EXAM_KEY";

    /**
     * Collect all core data and (in the future) send it to a central server.
     * Currently this only prepares the payload and writes it to a local file
     * for inspection.
     */
    public static boolean syncAllDataToServer(String adminId) {
        try {
            String payload = buildFullSnapshotJson();
            File outFile = writeBackupFile("sync-payload", payload);

            LogService.addLog(adminId, "prepared sync payload at " + outFile.getAbsolutePath());
            // TODO: Replace with real HTTP POST to your central server.
            // Example: HttpClient.post("https://your-server/api/sync/full", payload);
            return true;
        } catch (Exception e) {
            LogService.addLog(adminId, "sync to server failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Automatic backup hook. In a real implementation this would check
     * internet connectivity and last-backup time before calling backupToCloud.
     */
    public static void autoBackupIfOnline(String adminId) {
        // TODO: Implement real connectivity check (e.g., ping your server).
        if (isOnline()) {
            backupToCloud(adminId);
        } else {
            LogService.addLog(adminId, "auto-backup skipped (offline)");
        }
    }

    /**
     * Prepare a backup file that can be uploaded to Google Drive
     * by a server component or future enhancement.
     */
    public static boolean backupToCloud(String adminId) {
        try {
            String payload = buildFullSnapshotJson();
            File outFile = writeBackupFile("backup", payload);

            LogService.addLog(adminId, "created local backup file at " + outFile.getAbsolutePath());

            // Send backup notification email (uses Gmail SMTP credentials from environment variables)
            boolean emailed = EmailService.sendBackupEmail(outFile, adminId, "shemaarafati26@gmail.com");
            if (emailed) {
                LogService.addLog(adminId, "backup email notification sent to shemaarafati26@gmail.com");
            } else {
                LogService.addLog(adminId, "backup email notification failed");
            }

            boolean uploaded = uploadBackupFileToServer(outFile, adminId);
            if (uploaded) {
                LogService.addLog(adminId, "backup uploaded to backend successfully for Drive folder " + DRIVE_FOLDER_ID_PLACEHOLDER);
                return true;
            } else {
                LogService.addLog(adminId, "backup upload to backend failed");
                return false;
            }
        } catch (Exception e) {
            LogService.addLog(adminId, "backup to cloud failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String buildFullSnapshotJson() {
        // Minimal JSON-like structure (without external libraries)
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"generated_at\": \"")
          .append(LocalDateTime.now().toString()).append("\",\n");

        // Users
        List<User> users = UserService.getAllUsers();
        sb.append("  \"users\": [\n");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            sb.append("    {")
              .append("\"id\": \"").append(u.getUserId()).append("\",")
              .append(" \"username\": \"").append(escape(u.getUsername())).append("\",")
              .append(" \"role\": \"").append(u.getRole()).append("\"")
              .append(" }");
            if (i < users.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Courses
        List<Course> courses = CourseService.getAllCourses();
        sb.append("  \"courses\": [\n");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            sb.append("    {")
              .append("\"id\": ").append(c.getCourseId()).append(",")
              .append(" \"name\": \"").append(escape(c.getCourseName())).append("\",")
              .append(" \"description\": \"").append(escape(c.getDescription())).append("\"")
              .append(" }");
            if (i < courses.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Exams
        List<Exam> exams = ExamService.getAllExams();
        sb.append("  \"exams\": [\n");
        for (int i = 0; i < exams.size(); i++) {
            Exam ex = exams.get(i);
            sb.append("    {")
              .append("\"id\": ").append(ex.getExamId()).append(",")
              .append(" \"course_id\": ").append(ex.getCourseId()).append(",")
              .append(" \"name\": \"").append(escape(ex.getExamName())).append("\",")
              .append(" \"exam_date\": \"")
                    .append(ex.getExamDate() != null ? ex.getExamDate().toString() : "")
                    .append("\",")
              .append(" \"duration_minutes\": ").append(ex.getDurationMinutes()).append(",")
              .append(" \"total_marks\": ").append(ex.getTotalMarks()).append(",")
              .append(" \"created_by\": \"").append(escape(ex.getCreatedBy())).append("\"")
              .append(" }");
            if (i < exams.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Enrollments
        List<Enrollment> enrollments = EnrollmentService.getAllEnrollments();
        sb.append("  \"enrollments\": [\n");
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            sb.append("    {")
              .append("\"id\": ").append(e.getEnrollmentId()).append(",")
              .append(" \"student_id\": \"").append(escape(e.getStudentId())).append("\",")
              .append(" \"course_id\": ").append(e.getCourseId()).append(",")
              .append(" \"status\": \"").append(escape(e.getStatus())).append("\"")
              .append(" }");
            if (i < enrollments.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Results
        List<Result> results = ResultService.listAll();
        sb.append("  \"results\": [\n");
        for (int i = 0; i < results.size(); i++) {
            Result r = results.get(i);
            sb.append("    {")
              .append("\"id\": ").append(r.getResultId()).append(",")
              .append(" \"student_id\": \"").append(escape(r.getStudentId())).append("\",")
              .append(" \"exam_id\": ").append(r.getExamId()).append(",")
              .append(" \"score\": ").append(r.getScore())
              .append(" }");
            if (i < results.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");

        sb.append("}\n");
        return sb.toString();
    }

    private static File writeBackupFile(String prefix, String payload) throws IOException {
        String home = System.getProperty("user.home");
        File dir = new File(home, "offline_exam_backups");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create backup directory: " + dir.getAbsolutePath());
        }
        String ts = LocalDateTime.now().format(FILE_TS);
        File outFile = new File(dir, prefix + "-" + ts + ".json");

        try (FileWriter fw = new FileWriter(outFile)) {
            fw.write(payload);
        }
        return outFile;
    }

    private static boolean uploadBackupFileToServer(File backupFile, String adminId) {
        HttpURLConnection connection = null;
        try {
            String boundary = "----OfflineExamBoundary" + System.currentTimeMillis();
            URL url = new URL(BACKEND_BASE_URL + "/api/backup/upload");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("X-API-KEY", API_KEY);

            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                out.writeBytes("--" + boundary + "\r\n");
                out.writeBytes("Content-Disposition: form-data; name=\"adminId\"\r\n\r\n");
                out.writeBytes(adminId + "\r\n");

                out.writeBytes("--" + boundary + "\r\n");
                out.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + backupFile.getName() + "\"\r\n");
                out.writeBytes("Content-Type: application/json\r\n\r\n");

                try (FileInputStream fis = new FileInputStream(backupFile)) {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }
                out.writeBytes("\r\n");
                out.writeBytes("--" + boundary + "--\r\n");
            }

            int status = connection.getResponseCode();
            return status >= 200 && status < 300;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static boolean isOnline() {
        // TODO: Replace with a real connectivity check to BACKEND_BASE_URL.
        return true;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

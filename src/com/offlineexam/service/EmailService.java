package com.offlineexam.service;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT_SSL = 465; // SSL

    // Environment variable names for credentials
    private static final String ENV_USER = "OFFLINE_EXAM_SMTP_USER";
    private static final String ENV_PASS = "OFFLINE_EXAM_SMTP_PASS";

    /**
     * Send the given file as an email attachment to the specified recipient using
     * Gmail SMTP over SSL. Credentials are read from environment variables
     * OFFLINE_EXAM_SMTP_USER and OFFLINE_EXAM_SMTP_PASS.
     */
    public static boolean sendBackupEmail(File attachment, String adminId, String toEmail) {
        String username = System.getenv(ENV_USER);
        String password = System.getenv(ENV_PASS);

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            LogService.addLog(adminId, "EmailService: SMTP credentials not set in environment variables.");
            return false;
        }

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (SSLSocket socket = (SSLSocket) factory.createSocket(SMTP_HOST, SMTP_PORT_SSL)) {
            socket.startHandshake();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));

            if (!readOk(reader)) return false; // server greeting

            sendLine(writer, "EHLO offline-exam");
            if (!readMultilineOk(reader)) return false;

            // AUTH LOGIN
            sendLine(writer, "AUTH LOGIN");
            if (!readStartsWith(reader, "334")) return false;

            sendLine(writer, Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.US_ASCII)));
            if (!readStartsWith(reader, "334")) return false;

            sendLine(writer, Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.US_ASCII)));
            if (!readStartsWith(reader, "235")) return false;

            // MAIL FROM / RCPT TO
            sendLine(writer, "MAIL FROM:<" + username + ">");
            if (!readStartsWith(reader, "250")) return false;

            sendLine(writer, "RCPT TO:<" + toEmail + ">");
            if (!readStartsWith(reader, "250")) return false;

            sendLine(writer, "DATA");
            if (!readStartsWith(reader, "354")) return false;

            // Simple text email body with no MIME attachment (embed path and content note)
            String subject = "Offline Exam Backup";
            sendLine(writer, "Subject: " + subject);
            sendLine(writer, "From: " + username);
            sendLine(writer, "To: " + toEmail);
            sendLine(writer, "");
            sendLine(writer, "Backup file generated at: " + attachment.getAbsolutePath());
            sendLine(writer, "(This simple implementation does not attach the file; it reports its location on disk.)");
            sendLine(writer, ".");
            if (!readStartsWith(reader, "250")) return false;

            sendLine(writer, "QUIT");
            readOk(reader);

            LogService.addLog(adminId, "EmailService: backup email notification sent to " + toEmail);
            return true;
        } catch (IOException e) {
            LogService.addLog(adminId, "EmailService: failed to send email - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void sendLine(BufferedWriter writer, String line) throws IOException {
        writer.write(line + "\r\n");
        writer.flush();
    }

    private static boolean readOk(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        return line != null && line.startsWith("2");
    }

    private static boolean readStartsWith(BufferedReader reader, String prefix) throws IOException {
        String line = reader.readLine();
        return line != null && line.startsWith(prefix);
    }

    private static boolean readMultilineOk(BufferedReader reader) throws IOException {
        String line;
        boolean ok = false;
        do {
            line = reader.readLine();
            if (line == null) return false;
            if (line.length() >= 3 && line.charAt(3) == ' ') {
                ok = line.startsWith("250");
                break;
            }
        } while (true);
        return ok;
    }
}


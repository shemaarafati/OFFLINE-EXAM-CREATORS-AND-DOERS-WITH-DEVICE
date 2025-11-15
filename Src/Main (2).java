package com.offlineexam;

import com.offlineexam.util.DBConnection;
import com.offlineexam.view.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        // Test database connection
        DBConnection.testConnection();

        // Start the application on EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

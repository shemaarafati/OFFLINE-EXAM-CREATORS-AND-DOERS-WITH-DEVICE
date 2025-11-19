package com.offlineexam;

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

        // Ask client user for server IP/host before showing login
        String defaultHost = System.getProperty("exam.server.host", "127.0.0.1");
        String inputHost = JOptionPane.showInputDialog(
                null,
                "Enter server IP or hostname (e.g. 192.168.1.16)",
                defaultHost
        );
        if (inputHost == null || inputHost.trim().isEmpty()) {
            inputHost = defaultHost;
        }
        System.setProperty("exam.server.host", inputHost.trim());

        // Start the application on EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

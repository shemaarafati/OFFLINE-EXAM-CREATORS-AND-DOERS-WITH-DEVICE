package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login - Offline Exam System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> login());
        panel.add(loginBtn);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.addActionListener(e -> {
            new SignupFrame().setVisible(true);
        });
        panel.add(signupBtn);

        add(panel);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password");
            return;
        }

        AuthService authService = new AuthService();
        User user = authService.authenticate(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + user.getFullName());
            // Open appropriate dashboard based on role
            openDashboard(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "admin":
                new AdminDashboard().setVisible(true);
                break;
            case "teacher":
                new TeacherDashboard(user).setVisible(true);
                break;
            case "student":
                new StudentDashboard(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + user.getRole());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
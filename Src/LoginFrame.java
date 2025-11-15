package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Offline Exam System - Login");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 400, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(74, 144, 226), 0, getHeight(), new Color(42, 95, 165));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Close button
        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeBtn.setBackground(new Color(220, 80, 80));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(closeBtn);

        // Header
        JLabel headerLabel = new JLabel("EXAM SYSTEM", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Glass panel for form
        JPanel formPanel = createGlassPanel();
        formPanel.setLayout(new GridLayout(5, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Username
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setOpaque(false);
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(74, 144, 226)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        usernameField.setBackground(new Color(255, 255, 255, 200));
        userPanel.add(userIcon, BorderLayout.WEST);
        userPanel.add(usernameField, BorderLayout.CENTER);

        // Password
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setOpaque(false);
        JLabel passIcon = new JLabel("🔒");
        passIcon.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(74, 144, 226)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setBackground(new Color(255, 255, 255, 200));
        passPanel.add(passIcon, BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = createStyledButton("LOGIN", new Color(74, 144, 226));
        loginButton.addActionListener(e -> performLogin());

        JButton signupButton = createStyledButton("SIGN UP", new Color(46, 204, 113));
        signupButton.addActionListener(e -> openSignup());

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        formPanel.add(createFormLabel("Username"));
        formPanel.add(userPanel);
        formPanel.add(createFormLabel("Password"));
        formPanel.add(passPanel);
        formPanel.add(buttonPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createGlassPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(52, 73, 94));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", Color.RED);
            return;
        }

        AuthService authService = new AuthService();
        User user = authService.login(username, password);

        if (user != null) {
            showMessage("Login successful! Welcome " + user.getFullName(), new Color(46, 204, 113));
            openDashboard(user);
            dispose();
        } else {
            showMessage("Invalid username or password", Color.RED);
        }
    }

    private void showMessage(String message, Color color) {
        JOptionPane.showMessageDialog(this, message, "System Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSignup() {
        SwingUtilities.invokeLater(() -> {
            new SignupFrame().setVisible(true);
        });
    }

    private void openDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            switch (user.getRole()) {
                case ADMIN:
                    new AdminDashboard(user).setVisible(true);
                    break;
                case TEACHER:
                    new TeacherDashboard(user).setVisible(true);
                    break;
                case STUDENT:
                    new StudentDashboard(user).setVisible(true);
                    break;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Set global UI colors
                UIManager.put("Panel.background", new Color(245, 245, 245));
                UIManager.put("Button.background", new Color(74, 144, 226));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}

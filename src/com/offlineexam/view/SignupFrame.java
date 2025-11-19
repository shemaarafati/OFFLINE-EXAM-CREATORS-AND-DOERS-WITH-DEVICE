package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SignupFrame extends JFrame {
    private JTextField fullNameField, emailField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public SignupFrame() {
        setTitle("Create Account - Exam System");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 500, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(155, 89, 182), 0, getHeight(), new Color(142, 68, 173));
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
        closeBtn.addActionListener(e -> dispose());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(closeBtn);

        // Header
        JLabel headerLabel = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Glass panel for form
        JPanel formPanel = createGlassPanel();
        formPanel.setLayout(new GridLayout(7, 1, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Form fields with icons
        fullNameField = createStyledTextField("👤");
        emailField = createStyledTextField("📧");
        usernameField = createStyledTextField("👤");
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(155, 89, 182)),
                BorderFactory.createEmptyBorder(10, 40, 10, 10)
        ));
        passwordField.setBackground(new Color(255, 255, 255, 200));

        roleCombo = new JComboBox<>(new String[]{"STUDENT", "TEACHER"});
        roleCombo.setBackground(new Color(255, 255, 255, 200));
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(155, 89, 182)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Add fields to form with labels
        formPanel.add(createFormLabel("Full Name"));
        formPanel.add(createFieldWithIcon(fullNameField, "👤"));
        formPanel.add(createFormLabel("Email"));
        formPanel.add(createFieldWithIcon(emailField, "📧"));
        formPanel.add(createFormLabel("Username"));
        formPanel.add(createFieldWithIcon(usernameField, "👤"));
        formPanel.add(createFormLabel("Password"));
        formPanel.add(createFieldWithIcon(passwordField, "🔒"));
        formPanel.add(createFormLabel("Role"));
        formPanel.add(roleCombo);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        JButton createButton = createStyledButton("CREATE ACCOUNT", new Color(46, 204, 113));
        createButton.addActionListener(e -> createAccount());

        JButton cancelButton = createStyledButton("CANCEL", new Color(192, 57, 43));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createFieldWithIcon(JComponent field, String icon) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JTextField createStyledTextField(String icon) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(155, 89, 182)),
                BorderFactory.createEmptyBorder(10, 40, 10, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 200));
        return field;
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

    private void createAccount() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        User.Role role = User.Role.valueOf(roleCombo.getSelectedItem().toString());

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user (ID will be generated by database)
        User user = new User(username, password, fullName, email, role);
        AuthService authService = new AuthService();

        if (authService.register(user)) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nPlease login with your credentials.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create account. Username might already exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
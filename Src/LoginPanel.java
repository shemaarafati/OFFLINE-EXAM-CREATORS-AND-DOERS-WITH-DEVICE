package OFFLINE;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 245, 255));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Project Label
        JLabel projectLabel = new JLabel("OFFLINE_EXAM_DEVICE_MANAGER", SwingConstants.CENTER);
        projectLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        projectLabel.setForeground(new Color(0, 50, 100));
        projectLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(projectLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Login Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(0, 50, 100), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Labels and fields
        JLabel userLabel = new JLabel("Username:");
        userLabel.setIcon(new ImageIcon("resources/icons/user.png")); // Add user icon
        JLabel passLabel = new JLabel("Password:");
        passLabel.setIcon(new ImageIcon("resources/icons/lock.png")); // Add lock icon
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setIcon(new ImageIcon("resources/icons/role.png")); // Add role icon

        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "Teacher", "Student"});
        JButton loginBtn = new JButton("Login");

        // Grid layout
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(roleBox, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(loginBtn, gbc);

        // Forgot Password Link
        JLabel forgotPassLabel = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotPassLabel.setForeground(Color.BLUE.darker());
        forgotPassLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(forgotPassLabel, gbc);

        forgotPassLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showChangePasswordDialog();
            }
        });

        // Image Panel
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("resources/exam_logo.png"); // Replace path
        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setBorder(new EmptyBorder(0, 0, 0, 20));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 5; centerPanel.add(imageLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; centerPanel.add(formPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Login Button Action
        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            switch (role) {
                case "Admin" -> cardLayout.show(mainPanel, "admin");
                case "Teacher" -> cardLayout.show(mainPanel, "teacher");
                case "Student" -> cardLayout.show(mainPanel, "student");
            }
        });
    }

    // Change Password Dialog
    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField oldPassField = new JPasswordField();
        JPasswordField newPassField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Old Password:"));
        panel.add(oldPassField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPassField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());

            if (!username.isEmpty() && !oldPass.isEmpty() && !newPass.isEmpty()) {
                // Display wait message for admin confirmation
                JOptionPane.showMessageDialog(
                        this,
                        "Password change request submitted. Please wait for admin confirmation.",
                        "Request Submitted",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "All fields are required!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}

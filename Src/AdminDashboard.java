package OFFLINE;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AdminDashboard extends JPanel {
    public AdminDashboard(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Admin Dashboard - OFFLINE_EXAM_DEVICE_MANAGER", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JPanel managePanel = new JPanel();
        managePanel.setBorder(new TitledBorder("Manage Users"));
        managePanel.add(new JButton("Add Teacher"));
        managePanel.add(new JButton("Add Student"));
        managePanel.add(new JButton("Update User"));

        JPanel logsPanel = new JPanel();
        logsPanel.setBorder(new TitledBorder("User Logs"));
        logsPanel.add(new JButton("View Logs"));
        logsPanel.add(new JButton("Export Logs"));

        JPanel logoutPanel = new JPanel();
        logoutPanel.add(new JButton("Logout") {{
            addActionListener(e -> cardLayout.show(mainPanel, "login"));
        }});

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        centerPanel.add(managePanel);
        centerPanel.add(logsPanel);
        centerPanel.add(logoutPanel);

        add(centerPanel, BorderLayout.CENTER);
    }
}


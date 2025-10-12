

package OFFLINE;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class TeacherDashboard extends JPanel {
    public TeacherDashboard(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Teacher Dashboard - OFFLINE_EXAM_DEVICE_MANAGER", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.NORTH);

        JPanel coursePanel = new JPanel();
        coursePanel.setBorder(new TitledBorder("Courses"));
        coursePanel.add(new JButton("Create Course"));

        JPanel examPanel = new JPanel();
        examPanel.setBorder(new TitledBorder("Exams"));
        examPanel.add(new JButton("Create Exam"));

        JPanel resultsPanel = new JPanel();
        resultsPanel.setBorder(new TitledBorder("Results"));
        resultsPanel.add(new JButton("Evaluate"));

        JPanel logoutPanel = new JPanel();
        logoutPanel.add(new JButton("Logout") {{
            addActionListener(e -> cardLayout.show(mainPanel, "login"));
        }});

        JPanel centerPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        centerPanel.add(coursePanel);
        centerPanel.add(examPanel);
        centerPanel.add(resultsPanel);
        centerPanel.add(logoutPanel);

        add(centerPanel, BorderLayout.CENTER);
    }
}


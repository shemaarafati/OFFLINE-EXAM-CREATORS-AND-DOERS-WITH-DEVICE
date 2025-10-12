package OFFLINE;

import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Main() {
        frame = new JFrame("OFFLINE_EXAM_DEVICE_MANAGER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add panels
        mainPanel.add(new LoginPanel(cardLayout, mainPanel), "login");
        mainPanel.add(new AdminDashboard(cardLayout, mainPanel), "admin");
        mainPanel.add(new TeacherDashboard(cardLayout, mainPanel), "teacher");
        mainPanel.add(new StudentDashboard(cardLayout, mainPanel), "student");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

package com.offlineexam.view;

import com.offlineexam.model.Course;
import com.offlineexam.model.User;
import com.offlineexam.model.Enrollment;
import com.offlineexam.network.ClientConnection;
import com.offlineexam.network.NetworkConfig;
import com.offlineexam.service.CourseService;
import com.offlineexam.service.UserService;
import com.offlineexam.service.EnrollmentService;
import com.offlineexam.service.DashboardService;
import com.offlineexam.service.LogService;
import com.offlineexam.service.SyncService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.List;

// Custom renderer and editor for action buttons
class ButtonRenderer extends DefaultTableCellRenderer {
    private final JPanel panel;
    private final JButton editButton;
    private final JButton deleteButton;

    public ButtonRenderer() {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);

        editButton = createStyledCellButton("Edit", new Color(52, 152, 219));
        deleteButton = createStyledCellButton("Delete", new Color(231, 76, 60));

        panel.add(editButton);
        panel.add(deleteButton);
    }

    private JButton createStyledCellButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = color;
                if (getModel().isPressed()) {
                    base = color.darker();
                } else if (getModel().isRollover()) {
                    base = color.brighter();
                }

                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private JTable table;
    private int currentRow;
    private String currentTableType;
    private AdminDashboard adminDashboard;

    public ButtonEditor(String tableType, AdminDashboard dashboard) {
        this.currentTableType = tableType;
        this.adminDashboard = dashboard;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);

        editButton = new JButton("Edit") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = new Color(52, 152, 219);
                if (getModel().isPressed()) base = base.darker();
                else if (getModel().isRollover()) base = base.brighter();
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        editButton.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        editButton.setContentAreaFilled(false);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(this);

        deleteButton = new JButton("Delete") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = new Color(231, 76, 60);
                if (getModel().isPressed()) base = base.darker();
                else if (getModel().isRollover()) base = base.brighter();
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        deleteButton.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        deleteButton.setContentAreaFilled(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this);

        panel.add(editButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        this.currentRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();

        if (e.getSource() == editButton) {
            handleEditAction(currentRow);
        } else if (e.getSource() == deleteButton) {
            handleDeleteAction(currentRow);
        }
    }

    private void handleEditAction(int row) {
        switch (currentTableType) {
            case "courses":
                adminDashboard.editCourseFromTable(row);
                break;
            case "users":
                adminDashboard.editUserFromTable(row);
                break;
            case "students":
                adminDashboard.editStudentFromTable(row);
                break;
            case "teachers":
                adminDashboard.editTeacherFromTable(row);
                break;
        }
    }

    private void handleDeleteAction(int row) {
        int confirm = JOptionPane.showConfirmDialog(table,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            switch (currentTableType) {
                case "courses":
                    adminDashboard.deleteCourseFromTable(row);
                    break;
                case "users":
                    adminDashboard.deleteUserFromTable(row);
                    break;
                case "students":
                    adminDashboard.deleteStudentFromTable(row);
                    break;
                case "teachers":
                    adminDashboard.deleteTeacherFromTable(row);
                    break;
            }
        }
    }
}

public class AdminDashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private DefaultTableModel coursesModel, usersModel, studentsModel, teachersModel, enrollmentsModel;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("Admin Dashboard - Exam System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        loadData();
        // Trigger a lightweight automatic backup when admin opens the dashboard
        SyncService.autoBackupIfOnline(currentUser.getUserId());
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219), 0, getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content area with tabs
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create styled tabbed pane without headers
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(44, 62, 80));

        // Create all panels
        JPanel overviewPanel = createOverviewPanel();
        JPanel coursesPanel = createCoursesPanel();
        JPanel enrollmentsPanel = createEnrollmentsPanel();
        JPanel usersPanel = createUsersPanel();
        JPanel studentsPanel = createStudentsPanel();
        JPanel teachersPanel = createTeachersPanel();
        JPanel reportsPanel = createReportsPanel();

        // Add tabs
        tabbedPane.addTab("Overview", overviewPanel);
        tabbedPane.addTab("Courses", coursesPanel);
        tabbedPane.addTab("Enrollments", enrollmentsPanel);
        tabbedPane.addTab("All Users", usersPanel);
        tabbedPane.addTab("Students", studentsPanel);
        tabbedPane.addTab("Teachers", teachersPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        // Hide tab headers
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0;
            }
            @Override
            protected int calculateTabAreaWidth(int tabPlacement, int horizRunCount, int maxTabWidth) {
                return 0;
            }
        });

        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo and Welcome
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(44, 62, 80));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel("ADMIN DASHBOARD");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        welcomeLabel.setForeground(new Color(189, 195, 199));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        welcomePanel.add(logoLabel);
        welcomePanel.add(welcomeLabel);

        // Navigation buttons
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(44, 62, 80));
        navPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] menuItems = {
                "Overview",
                "Courses",
                "Enrollments",
                "All Users",
                "Students",
                "Teachers",
                "Reports"
        };

        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            final String tabName = item;
            menuButton.addActionListener(e -> switchToTab(tabName));
            navPanel.add(menuButton);
            navPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        sidebar.add(welcomePanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(navPanel);
        sidebar.add(Box.createVerticalGlue());

        // User info and logout at bottom
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(52, 73, 94));
        userPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        userPanel.setMaximumSize(new Dimension(250, 120));

        JLabel userInfoLabel = new JLabel("Administrator");
        userInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userIdLabel = new JLabel("ID: " + currentUser.getUserId());
        userIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        userIdLabel.setForeground(new Color(189, 195, 199));
        userIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = createMenuButton("Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());

        userPanel.add(userInfoLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userPanel.add(userIdLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userPanel.add(logoutBtn);

        sidebar.add(userPanel);

        return sidebar;
    }

    private void switchToTab(String tabName) {
        String[] tabNames = {
                "Overview",
                "Courses",
                "Enrollments",
                "All Users",
                "Students",
                "Teachers",
                "Reports"
        };
        for (int i = 0; i < tabNames.length; i++) {
            if (tabNames[i].equals(tabName)) {
                tabbedPane.setSelectedIndex(i);
                break;
            }
        }
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel welcomeLabel = new JLabel("Admin Dashboard - System Overview");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(44, 62, 80));

        JLabel subTitle = new JLabel("Manage courses, users, and system analytics");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(new Color(127, 140, 141));

        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(subTitle, BorderLayout.SOUTH);

        // Stats cards
        JPanel statsPanel = createAdminStatsPanel();
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAdminStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        int totalCourses = DashboardService.getTotalCourses();
        int totalStudents = DashboardService.getTotalStudents();
        int totalTeachers = DashboardService.getTotalTeachers();
        int totalExams = DashboardService.getTotalExams();
        int pendingRequests = DashboardService.getPendingRequests();
        int totalUsers = DashboardService.getTotalUsers();

        String[] statsData = {
                "Total Courses|Courses|" + totalCourses + "|52,152,219",
                "Total Students|Students|" + totalStudents + "|46,204,113",
                "Total Teachers|Teachers|" + totalTeachers + "|155,89,182",
                "Active Exams|Exams|" + totalExams + "|241,196,15",
                "Pending Requests|Requests|" + pendingRequests + "|230,126,34",
                "System Users|Users|" + totalUsers + "|52,152,219"
        };

        for (String data : statsData) {
            String[] parts = data.split("\\|");
            Color color = new Color(
                    Integer.parseInt(parts[3].split(",")[0].trim()),
                    Integer.parseInt(parts[3].split(",")[1].trim()),
                    Integer.parseInt(parts[3].split(",")[2].trim())
            );
            statsPanel.add(createStatCard(parts[0], parts[1], parts[2], color));
        }

        return statsPanel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Course ID", "Course Name", "Description", "Teacher", "Students", "Status", "Actions"};
        coursesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };
        JTable coursesTable = createStyledTable(coursesModel, "courses");

        // Set custom renderer and editor for actions column
        coursesTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        coursesTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("courses", this));

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton createCourseBtn = createActionButton("Create Course", new Color(46, 204, 113));
        createCourseBtn.addActionListener(e -> createCourse());

        JButton assignTeacherBtn = createActionButton("Assign Teacher", new Color(52, 152, 219));
        assignTeacherBtn.addActionListener(e -> {
            int selectedRow = coursesModel.getRowCount() > 0 ? ((JTable)((JScrollPane)((JPanel)((JButton)e.getSource()).getParent().getParent()).getComponent(0)).getViewport().getView()).getSelectedRow() : -1;
            if (selectedRow >= 0) {
                editCourseFromTable(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course row first.");
            }
        });

        JButton refreshBtn = createActionButton("Refresh", new Color(155, 89, 182));
        refreshBtn.addActionListener(e -> loadCourses());

        buttonPanel.add(createCourseBtn);
        buttonPanel.add(assignTeacherBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Enrollments tab - view and manage all pending enrollment requests (admin)
    private JPanel createEnrollmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Request ID", "Student", "Course", "Teacher", "Request Date", "Status"};
        enrollmentsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable enrollmentsTable = createStyledTable(enrollmentsModel, "enrollments");
        JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton approveBtn = createActionButton("Approve", new Color(39, 174, 96));
        approveBtn.addActionListener(e -> handleEnrollmentDecision(enrollmentsTable.getSelectedRow(), true));

        JButton rejectBtn = createActionButton("Reject", new Color(231, 76, 60));
        rejectBtn.addActionListener(e -> handleEnrollmentDecision(enrollmentsTable.getSelectedRow(), false));

        JButton refreshBtn = createActionButton("Refresh", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadEnrollments());

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"User ID", "Username", "Full Name", "Email", "Role", "Actions"};
        usersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
            }
        };
        JTable usersTable = createStyledTable(usersModel, "users");

        // Set custom renderer and editor for actions column
        usersTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        usersTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("users", this));

        JScrollPane scrollPane = new JScrollPane(usersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton createUserBtn = createActionButton("Create User", new Color(46, 204, 113));
        createUserBtn.addActionListener(e -> createUser());

        JButton refreshBtn = createActionButton("Refresh", new Color(155, 89, 182));
        refreshBtn.addActionListener(e -> loadAllUsers());

        buttonPanel.add(createUserBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Student ID", "Full Name", "Email", "Username", "Enrolled Courses", "Average Grade", "Actions"};
        studentsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };
        JTable studentsTable = createStyledTable(studentsModel, "students");

        // Set custom renderer and editor for actions column
        studentsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        studentsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("students", this));

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = createActionButton("Refresh", new Color(155, 89, 182));
        refreshBtn.addActionListener(e -> loadStudents());

        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTeachersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Teacher ID", "Full Name", "Email", "Username", "Assigned Courses", "Students", "Actions"};
        teachersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };
        JTable teachersTable = createStyledTable(teachersModel, "teachers");

        // Set custom renderer and editor for actions column
        teachersTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        teachersTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("teachers", this));

        JScrollPane scrollPane = new JScrollPane(teachersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = createActionButton("Refresh", new Color(155, 89, 182));
        refreshBtn.addActionListener(e -> loadTeachers());

        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("System Reports, Analytics & Logs");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBackground(Color.WHITE);

        JPanel reportsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        reportsPanel.setBackground(Color.WHITE);

        // Real numbers for each report card
        int totalStudents = DashboardService.getTotalStudents();
        int totalCourses = DashboardService.getTotalCourses();
        int totalTeachers = DashboardService.getTotalTeachers();
        int totalExams = DashboardService.getTotalExams();
        int totalUsers = DashboardService.getTotalUsers();

        reportsPanel.add(createReportCard(
                "Student Performance",
                "SP",
                "View student grades and performance metrics",
                String.valueOf(totalStudents)));

        reportsPanel.add(createReportCard(
                "Course Analytics",
                "CA",
                "Analyze course enrollment and completion rates",
                String.valueOf(totalCourses)));

        reportsPanel.add(createReportCard(
                "Teacher Reports",
                "TR",
                "Teacher performance and course assignments",
                String.valueOf(totalTeachers)));

        reportsPanel.add(createReportCard(
                "System Usage",
                "SU",
                "Platform usage statistics and trends",
                String.valueOf(totalUsers) + " users / " + totalExams + " exams"));

        // Logs area on the right
        JPanel logsPanel = new JPanel(new BorderLayout());
        logsPanel.setBackground(Color.WHITE);
        logsPanel.setBorder(BorderFactory.createTitledBorder("Recent System Logs"));

        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Consolas", Font.PLAIN, 11));

        JScrollPane logScroll = new JScrollPane(logsArea);
        logsPanel.add(logScroll, BorderLayout.CENTER);

        JButton refreshLogsBtn = createActionButton("Refresh Logs", new Color(52, 152, 219));
        refreshLogsBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (String log : LogService.getRecentLogs(100)) {
                sb.append(log).append("\n");
            }
            logsArea.setText(sb.toString());
            logsArea.setCaretPosition(logsArea.getDocument().getLength());
        });
        logsPanel.add(refreshLogsBtn, BorderLayout.SOUTH);

        // Initial load
        StringBuilder sb = new StringBuilder();
        for (String log : LogService.getRecentLogs(100)) {
            sb.append(log).append("\n");
        }
        logsArea.setText(sb.toString());

        centerPanel.add(reportsPanel, BorderLayout.CENTER);
        centerPanel.add(logsPanel, BorderLayout.EAST);

        // Export and Sync/Backup buttons along bottom
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        exportPanel.setBackground(Color.WHITE);

        JButton exportStudentBtn = createActionButton("Export Student Performance PDF", new Color(46, 204, 113));
        exportStudentBtn.addActionListener(e -> exportReportToPDF("Student Performance"));

        JButton exportCourseBtn = createActionButton("Export Course Analytics PDF", new Color(52, 152, 219));
        exportCourseBtn.addActionListener(e -> exportReportToPDF("Course Analytics"));

        JButton exportTeacherBtn = createActionButton("Export Teacher Reports PDF", new Color(155, 89, 182));
        exportTeacherBtn.addActionListener(e -> exportReportToPDF("Teacher Reports"));

        JButton exportUsageBtn = createActionButton("Export System Usage PDF", new Color(230, 126, 34));
        exportUsageBtn.addActionListener(e -> exportReportToPDF("System Usage"));

        // Sync and backup buttons (admin-only actions, but dashboard is already admin-only UI)
        JButton syncBtn = createActionButton("Sync Data to Server", new Color(41, 128, 185));
        syncBtn.addActionListener(e -> {
            boolean ok = SyncService.syncAllDataToServer(currentUser.getUserId());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Data sync payload prepared successfully.\nCheck backups folder or server logs for details.",
                        "Sync Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sync failed. Please check logs for details.",
                        "Sync Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backupBtn = createActionButton("Backup to Cloud", new Color(39, 174, 96));
        backupBtn.addActionListener(e -> {
            boolean ok = SyncService.backupToCloud(currentUser.getUserId());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Local backup file created successfully.\nFuture versions will upload this to Google Drive.",
                        "Backup Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Backup failed. Please check logs for details.",
                        "Backup Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        exportPanel.add(exportStudentBtn);
        exportPanel.add(exportCourseBtn);
        exportPanel.add(exportTeacherBtn);
        exportPanel.add(exportUsageBtn);
        exportPanel.add(syncBtn);
        exportPanel.add(backupBtn);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(exportPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportCard(String title, String icon, String description, String metric) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));

        JLabel metricLabel = new JLabel(metric);
        metricLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        metricLabel.setForeground(new Color(52, 73, 94));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(metricLabel, BorderLayout.CENTER);
        textPanel.add(descLabel, BorderLayout.SOUTH);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void exportReportToPDF(String reportType) {
        StringBuilder sb = new StringBuilder();
        sb.append(reportType).append(" Report\n\n");

        // Basic summary based on DashboardService stats
        sb.append("Generated on: ").append(java.time.LocalDateTime.now()).append("\n\n");

        sb.append("Total Courses: ").append(DashboardService.getTotalCourses()).append("\n");
        sb.append("Total Students: ").append(DashboardService.getTotalStudents()).append("\n");
        sb.append("Total Teachers: ").append(DashboardService.getTotalTeachers()).append("\n");
        sb.append("Total Exams: ").append(DashboardService.getTotalExams()).append("\n");
        sb.append("Total Users: ").append(DashboardService.getTotalUsers()).append("\n");

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 10));

        try {
            MessageFormat header = new MessageFormat(reportType + " - Offline Exam System");
            boolean printed = area.print(header, null, true, null, null, true);
            if (printed) {
                JOptionPane.showMessageDialog(this,
                        "Send to a PDF printer in the print dialog to save as PDF.",
                        "Export Started",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to print/export report: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // CRUD Operations Implementation
    private void createCourse() {
        JTextField nameField = new JTextField(20);
        JTextField codeField = new JTextField(20);
        JTextField descField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Course Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course Code (optional):"));
        panel.add(codeField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Create New Course", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            String description = descField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course name cannot be empty");
                return;
            }

            Course course = new Course(name, code, description);
            if (CourseService.createCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course created successfully");
                LogService.addLog("Admin " + currentUser.getUserId() + " created course '" + name + "'.");
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create course");
            }
        }
    }

    private void createUser() {
        JTextField fullNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"STUDENT", "TEACHER"});

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Create New User", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            User.Role role = User.Role.valueOf(roleCombo.getSelectedItem().toString());

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            User newUser = new User(username, password, fullName, email, role);
            boolean ok = false;
            try {
                ClientConnection connection = new ClientConnection(NetworkConfig.getServerHost(), NetworkConfig.getServerPort());
                ok = connection.createUser(newUser);
                connection.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot connect to server at 127.0.0.1:5000", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ok) {
                JOptionPane.showMessageDialog(this, "User created successfully");
                LogService.addLog("Admin " + currentUser.getUserId() + " created user '" + username + "' with role " + role + ".");
                loadAllUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create user");
            }
        }
    }

    // Table-based edit methods
    public void editCourseFromTable(int row) {
        String courseId = coursesModel.getValueAt(row, 0).toString();
        String courseName = coursesModel.getValueAt(row, 1).toString();
        String description = coursesModel.getValueAt(row, 2).toString();

        JTextField nameField = new JTextField(courseName, 20);
        JTextField descField = new JTextField(description, 20);

        // Build teacher combo box
        List<User> teachers = UserService.getUsersByRole(User.Role.TEACHER);
        DefaultComboBoxModel<String> teacherModel = new DefaultComboBoxModel<>();
        teacherModel.addElement("(Not assigned)");
        for (User t : teachers) {
            teacherModel.addElement(t.getUserId() + " - " + t.getUsername());
        }
        JComboBox<String> teacherCombo = new JComboBox<>(teacherModel);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Course Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Teacher:"));
        panel.add(teacherCombo);
        panel.add(new JLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit Course", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newDesc = descField.getText().trim();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course name cannot be empty");
                return;
            }

            // Parse selected teacher
            String selected = (String) teacherCombo.getSelectedItem();
            String teacherId = null;
            if (selected != null && !selected.equals("(Not assigned)")) {
                int idx = selected.indexOf(' ');
                teacherId = (idx > 0) ? selected.substring(0, idx) : selected;
            }

            boolean okMain = CourseService.updateCourse(courseId, newName, null, newDesc);
            boolean okAssign = CourseService.assignCourseTeacher(courseId, teacherId);

            if (okMain || okAssign) {
                coursesModel.setValueAt(newName, row, 1);
                coursesModel.setValueAt(newDesc, row, 2);
                String teacherDisplay = (teacherId == null) ? "Not assigned" : selected;
                coursesModel.setValueAt(teacherDisplay, row, 3);
                JOptionPane.showMessageDialog(this, "Course updated successfully");
                LogService.addLog("Admin " + currentUser.getUserId() + " updated course ID " + courseId + ".");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update course");
            }
        }
    }

    public void editUserFromTable(int row) {
        String userId = usersModel.getValueAt(row, 0).toString();
        String username = usersModel.getValueAt(row, 1).toString();
        String fullName = usersModel.getValueAt(row, 2).toString();
        String email = usersModel.getValueAt(row, 3).toString();

        JTextField nameField = new JTextField(fullName, 20);
        JTextField emailField = new JTextField(email, 20);
        JTextField usernameField = new JTextField(username, 20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("New Password (leave blank to keep current):"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit User", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword());

            if (newName.isEmpty() || newEmail.isEmpty() || newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields except password are required");
                return;
            }

            // Update the table model
            usersModel.setValueAt(newUsername, row, 1);
            usersModel.setValueAt(newName, row, 2);
            usersModel.setValueAt(newEmail, row, 3);

            // Update database
            boolean ok = UserService.updateUserProfile(userId, newUsername, newPassword, newName, newEmail);

            if (ok) {
                JOptionPane.showMessageDialog(this, "User updated successfully");
                LogService.addLog("Admin " + currentUser.getUserId() + " updated user ID " + userId + ".");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user");
            }
        }
    }

    public void editStudentFromTable(int row) {
        String studentId = studentsModel.getValueAt(row, 0).toString();
        String fullName = studentsModel.getValueAt(row, 1).toString();
        String email = studentsModel.getValueAt(row, 2).toString();

        JTextField nameField = new JTextField(fullName, 20);
        JTextField emailField = new JTextField(email, 20);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit Student", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            // Update the table model
            studentsModel.setValueAt(newName, row, 1);
            studentsModel.setValueAt(newEmail, row, 2);

            // Update database
            if (UserService.updateUserProfile(studentId, null, null, newName, newEmail)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student");
            }
        }
    }

    public void editTeacherFromTable(int row) {
        String teacherId = teachersModel.getValueAt(row, 0).toString();
        String fullName = teachersModel.getValueAt(row, 1).toString();
        String email = teachersModel.getValueAt(row, 2).toString();

        JTextField nameField = new JTextField(fullName, 20);
        JTextField emailField = new JTextField(email, 20);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit Teacher", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            // Update the table model
            teachersModel.setValueAt(newName, row, 1);
            teachersModel.setValueAt(newEmail, row, 2);

            // Update database
            if (UserService.updateUserProfile(teacherId, null, null, newName, newEmail)) {
                JOptionPane.showMessageDialog(this, "Teacher updated successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update teacher");
            }
        }
    }

    // Table-based delete methods
    public void deleteCourseFromTable(int row) {
        String courseId = coursesModel.getValueAt(row, 0).toString();
        String courseName = coursesModel.getValueAt(row, 1).toString();

        if (CourseService.deleteCourse(courseId)) {
            coursesModel.removeRow(row);
            JOptionPane.showMessageDialog(this, "Course deleted successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete course");
        }
    }

    public void deleteUserFromTable(int row) {
        String userId = usersModel.getValueAt(row, 0).toString();
        String userName = usersModel.getValueAt(row, 2).toString();
        boolean ok = false;
        try {
            ClientConnection connection = new ClientConnection(NetworkConfig.getServerHost(), NetworkConfig.getServerPort());
            ok = connection.deleteUser(userId);
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server at 127.0.0.1:5000", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ok) {
            usersModel.removeRow(row);
            JOptionPane.showMessageDialog(this, "User deleted successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete user");
        }
    }

    public void deleteStudentFromTable(int row) {
        String studentId = studentsModel.getValueAt(row, 0).toString();
        String studentName = studentsModel.getValueAt(row, 1).toString();
        boolean ok = false;
        try {
            ClientConnection connection = new ClientConnection(NetworkConfig.getServerHost(), NetworkConfig.getServerPort());
            ok = connection.deleteUser(studentId);
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server at 127.0.0.1:5000", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ok) {
            studentsModel.removeRow(row);
            JOptionPane.showMessageDialog(this, "Student deleted successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete student");
        }
    }

    public void deleteTeacherFromTable(int row) {
        String teacherId = teachersModel.getValueAt(row, 0).toString();
        String teacherName = teachersModel.getValueAt(row, 1).toString();
        boolean ok = false;
        try {
            ClientConnection connection = new ClientConnection(NetworkConfig.getServerHost(), NetworkConfig.getServerPort());
            ok = connection.deleteUser(teacherId);
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server at 127.0.0.1:5000", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ok) {
            teachersModel.removeRow(row);
            JOptionPane.showMessageDialog(this, "Teacher deleted successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete teacher");
        }
    }

    // Data loading methods
    private void loadData() {
        loadCourses();
        loadEnrollments();
        loadAllUsers();
        loadStudents();
        loadTeachers();
    }

    private void loadCourses() {
        coursesModel.setRowCount(0);
        List<Course> courses = CourseService.getAllCourses();

        // Build a simple lookup of teacherId -> display name (username)
        List<User> teachers = UserService.getUsersByRole(User.Role.TEACHER);
        java.util.Map<String, String> teacherMap = new java.util.HashMap<>();
        for (User t : teachers) {
            teacherMap.put(t.getUserId(), t.getUsername());
        }

        for (Course course : courses) {
            String teacherId = course.getCreatedBy();
            String teacherDisplay = (teacherId != null && teacherMap.containsKey(teacherId))
                    ? teacherId + " - " + teacherMap.get(teacherId)
                    : "Not assigned";
            int studentCount = EnrollmentService.countApprovedEnrollmentsByCourse(course.getCourseId());
            String status;
            if (teacherId == null) {
                status = "No teacher";
            } else if (studentCount > 0) {
                status = "Active";
            } else {
                status = "No students";
            }

            coursesModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getDescription(),
                    teacherDisplay,
                    studentCount + " students",
                    status,
                    "" // Empty string for actions column
            });
        }
    }

    // Load all pending enrollments for the Enrollments tab
    private void loadEnrollments() {
        if (enrollmentsModel == null) {
            return;
        }
        enrollmentsModel.setRowCount(0);

        java.util.List<Enrollment> pending = EnrollmentService.getAllPendingEnrollments();

        for (Enrollment e : pending) {
            User student = UserService.getUserById(e.getStudentId());
            String studentLabel = (student != null)
                    ? student.getUserId() + " - " + student.getUsername()
                    : e.getStudentId();

            Course course = CourseService.getCourseById(e.getCourseId());
            String courseLabel = (course != null)
                    ? course.getCourseId() + " - " + course.getCourseName()
                    : String.valueOf(e.getCourseId());

            String teacherLabel = "N/A";
            if (course != null && course.getCreatedBy() != null) {
                User teacher = UserService.getUserById(course.getCreatedBy());
                if (teacher != null) {
                    teacherLabel = teacher.getUserId() + " - " + teacher.getUsername();
                } else {
                    teacherLabel = course.getCreatedBy();
                }
            }

            String date = e.getRequestDate() != null
                    ? e.getRequestDate().toString()
                    : "N/A";

            enrollmentsModel.addRow(new Object[]{
                    e.getEnrollmentId(),
                    studentLabel,
                    courseLabel,
                    teacherLabel,
                    date,
                    e.getStatus()
            });
        }
    }

    // Approve or reject a selected enrollment request
    private void handleEnrollmentDecision(int selectedRow, boolean approve) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a request first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int enrollmentId = (int) enrollmentsModel.getValueAt(selectedRow, 0);
        String action = approve ? "APPROVED" : "REJECTED";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + action.toLowerCase() + " this enrollment request?",
                approve ? "Confirm Approval" : "Confirm Rejection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = EnrollmentService.updateEnrollmentStatus(enrollmentId, action, currentUser.getUserId());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Enrollment request " + action.toLowerCase() + " successfully.");
                loadEnrollments();
                loadCourses(); // refresh course stats
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update enrollment request.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadAllUsers() {
        usersModel.setRowCount(0);
        List<User> users;
        try {
            ClientConnection connection = new ClientConnection(NetworkConfig.getServerHost(), NetworkConfig.getServerPort());
            users = connection.getAllUsers();
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server at 127.0.0.1:5000", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (User user : users) {
            usersModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().toString(),
                    "" // Empty string for actions column
            });
        }
    }

    private void loadStudents() {
        studentsModel.setRowCount(0);
        List<User> students = UserService.getUsersByRole(User.Role.STUDENT);

        for (User student : students) {
            studentsModel.addRow(new Object[]{
                    student.getUserId(),
                    student.getFullName(),
                    student.getEmail(),
                    student.getUsername(),
                    (int)(Math.random() * 8) + 2 + " courses",
                    (int)(Math.random() * 30) + 70 + "%",
                    "" // Empty string for actions column
            });
        }
    }

    private void loadTeachers() {
        teachersModel.setRowCount(0);
        List<User> teachers = UserService.getUsersByRole(User.Role.TEACHER);

        for (User teacher : teachers) {
            // Get real assigned courses for this teacher
            List<Course> teacherCourses = CourseService.getCoursesByTeacher(teacher.getUserId());
            int courseCount = teacherCourses != null ? teacherCourses.size() : 0;

            // Sum enrolled students across all assigned courses
            int totalStudents = 0;
            if (teacherCourses != null) {
                for (Course c : teacherCourses) {
                    totalStudents += EnrollmentService.countApprovedEnrollmentsByCourse(c.getCourseId());
                }
            }

            teachersModel.addRow(new Object[]{
                    teacher.getUserId(),
                    teacher.getFullName(),
                    teacher.getEmail(),
                    teacher.getUsername(),
                    courseCount + " courses",
                    totalStudents + " students",
                    "" // Empty string for actions column
            });
        }
    }

    // Utility methods
    private JPanel createStatCard(String title, String icon, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(valueLabel, BorderLayout.NORTH);
        textPanel.add(titleLabel, BorderLayout.SOUTH);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JTable createStyledTable(DefaultTableModel model, String tableType) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(248, 248, 248) : Color.WHITE);
                }
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(236, 240, 241));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);
        java.awt.Dimension headerSize = header.getPreferredSize();
        header.setPreferredSize(new java.awt.Dimension(headerSize.width, 32));

        // Custom header renderer: solid background, centered white text, subtle grid border
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setOpaque(true);
        headerRenderer.setBackground(new Color(52, 152, 219));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(236, 240, 241)));

        header.setDefaultRenderer(headerRenderer);

        return table;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(52, 152, 219));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(52, 73, 94));
                } else {
                    g2.setColor(new Color(52, 73, 94));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(250, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    private JButton createActionButton(String text, Color color) {
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

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                // Replace with your actual login frame
                // new LoginFrame().setVisible(true);
            });
            JOptionPane.showMessageDialog(null, "You have been logged out successfully.");
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        SwingUtilities.invokeLater(() -> {
            User testUser = new User("admin1", "password", "Administrator", "admin@system.com", User.Role.ADMIN);
            new AdminDashboard(testUser).setVisible(true);
        });
    }
}

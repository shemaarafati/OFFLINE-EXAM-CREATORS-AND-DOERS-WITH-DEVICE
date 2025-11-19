package com.offlineexam.view;

import com.offlineexam.model.*;
import com.offlineexam.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private DefaultTableModel coursesModel, examsModel, resultsModel, scheduleModel;
    private JPanel overviewPanel;

    public StudentDashboard(User user) {
        this.currentUser = user;
        setTitle("Student Dashboard - Exam System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        loadData();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(230, 126, 34), 0, getHeight(), new Color(211, 84, 0));
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

        // Remove tab headers
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));

        // Create all panels
        overviewPanel = createOverviewPanel();
        JPanel coursesPanel = createCoursesPanel();
        JPanel examsPanel = createExamsPanel();
        JPanel resultsPanel = createResultsPanel();
        JPanel schedulePanel = createSchedulePanel();
        JPanel settingsPanel = createSettingsPanel();

        // Add tabs
        tabbedPane.addTab("Overview", overviewPanel);
        tabbedPane.addTab("Courses", coursesPanel);
        tabbedPane.addTab("Exams", examsPanel);
        tabbedPane.addTab("Results", resultsPanel);
        tabbedPane.addTab("Schedule", schedulePanel);
        tabbedPane.addTab("Settings", settingsPanel);

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

        JLabel logoLabel = new JLabel("STUDENT DASHBOARD");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Hello, " + currentUser.getFullName());
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
                "Exams",
                "Results",
                "Schedule",
                "Settings"
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

        JLabel userInfoLabel = new JLabel("Student Information");
        userInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userIdLabel = new JLabel("ID: " + currentUser.getUserId());
        userIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        userIdLabel.setForeground(new Color(189, 195, 199));
        userIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userEmailLabel = new JLabel(currentUser.getEmail());
        userEmailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        userEmailLabel.setForeground(new Color(189, 195, 199));
        userEmailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = createMenuButton("Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());

        userPanel.add(userInfoLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userPanel.add(userIdLabel);
        userPanel.add(userEmailLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userPanel.add(logoutBtn);

        sidebar.add(userPanel);

        return sidebar;
    }

    private void switchToTab(String tabName) {
        String[] tabNames = {
                "Overview",
                "Courses",
                "Exams",
                "Results",
                "Schedule",
                "Settings"
        };
        for (int i = 0; i < tabNames.length; i++) {
            if (tabNames[i].equals(tabName)) {
                tabbedPane.setSelectedIndex(i);

                // Refresh data when navigating between tabs
                if ("Courses".equals(tabName)) {
                    loadAvailableCourses();
                } else if ("Exams".equals(tabName)) {
                    loadMyExams();
                } else if ("Results".equals(tabName)) {
                    loadMyResults();
                } else if ("Schedule".equals(tabName)) {
                    loadSchedule();
                }
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

        JLabel welcomeLabel = new JLabel("Welcome Back, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(44, 62, 80));

        JLabel subTitle = new JLabel("Here's your academic overview");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(new Color(127, 140, 141));

        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(subTitle, BorderLayout.SOUTH);

        // Stats cards
        JPanel statsPanel = createStudentStatsPanel();

        // Quick actions
        JPanel actionsPanel = createQuickActionsPanel();

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        // Compute real stats from database
        int enrolledCourses = 0;
        int examsTaken = 0;
        double averageScore = 0.0;
        int pendingExams = 0;

        try {
            // Enrolled courses (APPROVED enrollments)
            java.util.List<Enrollment> enrollments = EnrollmentService.getStudentEnrollments(currentUser.getUserId());
            enrolledCourses = enrollments != null ? enrollments.size() : 0;

            // Results and exams taken
            java.util.List<Result> results = ResultService.listByStudent(currentUser.getUserId());
            examsTaken = results != null ? results.size() : 0;

            double totalScore = 0.0;
            if (results != null && !results.isEmpty()) {
                for (Result r : results) {
                    totalScore += r.getScore();
                }
                averageScore = totalScore / results.size();
            }

            // Pending exams: upcoming/available exams for enrolled courses where no result yet
            java.time.LocalDate today = java.time.LocalDate.now();
            int pendingCount = 0;
            if (enrollments != null) {
                for (Enrollment enrollment : enrollments) {
                    int courseId = enrollment.getCourseId();
                    java.util.List<Exam> exams = ExamService.getExamsByCourse(courseId);
                    for (Exam exam : exams) {
                        java.time.LocalDate examDate = exam.getExamDate();
                        if (!examDate.isBefore(today)) {
                            Result existing = ResultService.findByStudentAndExam(currentUser.getUserId(), exam.getExamId());
                            if (existing == null) {
                                pendingCount++;
                            }
                        }
                    }
                }
            }
            pendingExams = pendingCount;
        } catch (Exception e) {
            System.err.println("Failed to compute student stats: " + e.getMessage());
        }

        // Build cards using dynamic values
        statsPanel.add(createStatCard("Enrolled Courses", "🎓", String.valueOf(enrolledCourses), new Color(230, 126, 34)));
        statsPanel.add(createStatCard("Exams Taken", "📝", String.valueOf(examsTaken), new Color(52, 152, 219)));

        String avgLabel = examsTaken > 0 ? String.format("%.0f%%", averageScore) : "N/A";
        statsPanel.add(createStatCard("Average Score", "⭐", avgLabel, new Color(46, 204, 113)));

        statsPanel.add(createStatCard("Pending Exams", "⏰", String.valueOf(pendingExams), new Color(155, 89, 182)));

        return statsPanel;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] actions = {
                "View Courses||View all available courses",
                "Take Exam||Start a new exam",
                "Check Results||View your exam results"
        };

        for (String action : actions) {
            String[] parts = action.split("\\|");
            panel.add(createActionCard(parts[0], parts[1], parts[2]));
        }

        return panel;
    }

    private JPanel createActionCard(String title, String icon, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon == null ? "" : icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(44, 62, 80));

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        descLabel.setForeground(new Color(127, 140, 141));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.SOUTH);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        // Add click action
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (title.contains("Courses")) switchToTab("Courses");
                else if (title.contains("Exam")) switchToTab("Exams");
                else if (title.contains("Results")) switchToTab("Results");
            }
        });

        return card;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Course ID", "Course Name", "Description", "Instructor", "Enrollment Status"};
        coursesModel = new DefaultTableModel(columns, 0);
        JTable coursesTable = createStyledTable(coursesModel);

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton enrollBtn = createActionButton("Enroll in Course", new Color(46, 204, 113));
        enrollBtn.addActionListener(e -> enrollInCourse(coursesTable.getSelectedRow()));

        JButton refreshBtn = createActionButton("Refresh Courses", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadAvailableCourses());

        JButton viewDetailsBtn = createActionButton("View Details", new Color(155, 89, 182));
        viewDetailsBtn.addActionListener(e -> viewCourseDetails(coursesTable.getSelectedRow()));

        buttonPanel.add(enrollBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createExamsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Exam ID", "Exam Name", "Course", "Date", "Duration", "Status"};
        examsModel = new DefaultTableModel(columns, 0);
        JTable examsTable = createStyledTable(examsModel);

        // Always show the table; it will be populated by loadMyExams()
        JScrollPane scrollPane = new JScrollPane(examsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton takeExamBtn = createActionButton("Take Exam", new Color(46, 204, 113));
        takeExamBtn.addActionListener(e -> takeExam(examsTable.getSelectedRow()));

        JButton refreshBtn = createActionButton("Refresh Exams", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadMyExams());

        JButton viewInstructionsBtn = createActionButton("Instructions", new Color(155, 89, 182));
        viewInstructionsBtn.addActionListener(e -> showExamInstructions(examsTable.getSelectedRow()));

        buttonPanel.add(takeExamBtn);
        buttonPanel.add(viewInstructionsBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Exam", "Course", "Score", "Grade", "Date Taken", "Status"};
        resultsModel = new DefaultTableModel(columns, 0);
        JTable resultsTable = createStyledTable(resultsModel);

        // Always show the table; it will be populated by loadMyResults()
        JScrollPane resultsScroll = new JScrollPane(resultsTable);
        panel.add(resultsScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = createActionButton("Refresh Results", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadMyResults());

        JButton viewDetailsBtn = createActionButton("View Details", new Color(155, 89, 182));
        viewDetailsBtn.addActionListener(e -> viewResultDetails(resultsTable.getSelectedRow()));

        JButton downloadBtn = createActionButton("Download Report", new Color(230, 126, 34));
        downloadBtn.addActionListener(e -> downloadResults());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(downloadBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Exam Name", "Course", "Exam Date", "Duration", "Instructor"};
        scheduleModel = new DefaultTableModel(columns, 0);
        JTable scheduleTable = createStyledTable(scheduleModel);

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        // Always show the table; it will be populated by loadSchedule()
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = createActionButton("Refresh Schedule", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadSchedule());
        panel.add(refreshBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Profile Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Profile form in a card
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 240, 241), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBackground(Color.WHITE);

        JTextField fullNameField = new JTextField(currentUser.getFullName());
        JTextField emailField = new JTextField(currentUser.getEmail());
        JTextField usernameField = new JTextField(currentUser.getUsername());
        JPasswordField passwordField = new JPasswordField();
        JTextField studentIdField = new JTextField(String.valueOf(currentUser.getUserId()));
        studentIdField.setEditable(false);
        studentIdField.setBackground(new Color(248, 249, 250));

        // Style form fields
        Component[] fields = {fullNameField, emailField, usernameField, passwordField, studentIdField};
        for (Component field : fields) {
            if (field instanceof JTextField) {
                ((JTextField) field).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(206, 212, 218)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                ((JTextField) field).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            } else if (field instanceof JPasswordField) {
                ((JPasswordField) field).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(206, 212, 218)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                ((JPasswordField) field).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }

        formPanel.add(createFormLabel("Full Name:"));
        formPanel.add(fullNameField);
        formPanel.add(createFormLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(createFormLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(createFormLabel("New Password:"));
        formPanel.add(passwordField);
        formPanel.add(createFormLabel("Student ID:"));
        formPanel.add(studentIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelBtn = createActionButton("Cancel", new Color(108, 117, 125));
        cancelBtn.addActionListener(e -> resetForm(fullNameField, emailField, usernameField, passwordField));

        JButton saveBtn = createActionButton("Save Changes", new Color(46, 204, 113));
        saveBtn.addActionListener(e -> saveProfileChanges(fullNameField.getText(), emailField.getText(),
                usernameField.getText(), new String(passwordField.getPassword())));

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        formCard.add(formPanel, BorderLayout.CENTER);
        formCard.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formCard, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(73, 80, 87));
        return label;
    }

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

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(248, 248, 248) : Color.WHITE);
                }

                // Color code grades and status
                if (getColumnName(column).equals("Grade")) {
                    String grade = getValueAt(row, column).toString();
                    if (grade.startsWith("A")) c.setForeground(new Color(46, 204, 113));
                    else if (grade.startsWith("B")) c.setForeground(new Color(52, 152, 219));
                    else if (grade.startsWith("C")) c.setForeground(new Color(230, 126, 34));
                    else c.setForeground(new Color(231, 76, 60));
                } else if (getColumnName(column).equals("Status")) {
                    String status = getValueAt(row, column).toString();
                    if (status.equals("Completed") || status.equals("Available")) c.setForeground(new Color(46, 204, 113));
                    else if (status.equals("In Progress") || status.equals("Upcoming")) c.setForeground(new Color(52, 152, 219));
                    else c.setForeground(new Color(231, 76, 60));
                } else {
                    c.setForeground(new Color(44, 62, 80));
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

        // Custom header renderer for solid background and centered text
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerRenderer.setOpaque(true);
        headerRenderer.setBackground(new Color(230, 126, 34));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(214, 116, 31)));

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
                    g2.setColor(new Color(230, 126, 34));
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

    private void resetForm(JTextField fullName, JTextField email, JTextField username, JPasswordField password) {
        fullName.setText(currentUser.getFullName());
        email.setText(currentUser.getEmail());
        username.setText(currentUser.getUsername());
        password.setText("");
        JOptionPane.showMessageDialog(this, "Form reset to original values", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveProfileChanges(String fullName, String email, String username, String password) {
        // Validation
        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (Full Name, Email, Username)",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Update user in database
            boolean success = UserService.updateUserProfile(
                    String.valueOf(currentUser.getUserId()),
                    username,
                    password.isEmpty() ? null : password, // Only update password if provided
                    fullName,
                    email
            );

            if (success) {
                // Update current user object
                currentUser.setFullName(fullName);
                currentUser.setEmail(email);
                currentUser.setUsername(username);
                if (!password.isEmpty()) {
                    currentUser.setPassword(password);
                }

                JOptionPane.showMessageDialog(this,
                        "Profile updated successfully in database!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the sidebar welcome message
                refreshSidebar();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update profile. Please try again.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating profile: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void refreshSidebar() {
        // This would typically involve recreating or updating the sidebar
        // For now, we'll just show a message
        System.out.println("Sidebar refreshed with new user data: " + currentUser.getFullName());
    }

    private void loadData() {
        loadAvailableCourses();
        loadMyExams();
        loadMyResults();
        loadSchedule();
    }

    private void loadAvailableCourses() {
        coursesModel.setRowCount(0);
        List<Course> courses = CourseService.getAllCourses();

        if (courses.isEmpty()) {
            return;
        }

        // Build a simple lookup of teacherId -> display name (username)
        java.util.List<User> teachers = UserService.getUsersByRole(User.Role.TEACHER);
        java.util.Map<String, String> teacherMap = new java.util.HashMap<>();
        for (User t : teachers) {
            teacherMap.put(t.getUserId(), t.getUsername());
        }

        for (Course course : courses) {
            String teacherId = course.getCreatedBy();
            String instructorName;
            if (teacherId != null && teacherMap.containsKey(teacherId)) {
                instructorName = teacherId + " - " + teacherMap.get(teacherId);
            } else if (teacherId != null) {
                instructorName = teacherId;
            } else {
                instructorName = "Not assigned";
            }

            String status = EnrollmentService.getEnrollmentStatus(currentUser.getUserId(), course.getCourseId());
            String enrollmentStatus;
            if (status == null) {
                enrollmentStatus = "Available";
            } else if ("PENDING".equalsIgnoreCase(status)) {
                enrollmentStatus = "Pending";
            } else if ("APPROVED".equalsIgnoreCase(status)) {
                enrollmentStatus = "Enrolled";
            } else {
                enrollmentStatus = status;
            }

            coursesModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getDescription(),
                    instructorName,
                    enrollmentStatus
            });
        }
    }

    private void loadMyExams() {
        examsModel.setRowCount(0);
        // Exams for courses where this student is APPROVED
        List<Enrollment> enrollments = EnrollmentService.getStudentEnrollments(currentUser.getUserId());
        if (enrollments.isEmpty()) {
            return;
        }

        java.time.LocalDate today = java.time.LocalDate.now();

        for (Enrollment enrollment : enrollments) {
            int courseId = enrollment.getCourseId();
            Course course = CourseService.getCourseById(courseId);
            String courseName = course != null ? course.getCourseName() : "Unknown";

            List<Exam> exams = ExamService.getExamsByCourse(courseId);
            for (Exam exam : exams) {
                String status;
                if (exam.getExamDate().isBefore(today)) {
                    status = "Completed";
                } else if (exam.getExamDate().isEqual(today)) {
                    status = "Available";
                } else {
                    status = "Upcoming";
                }

                examsModel.addRow(new Object[]{
                        exam.getExamId(),
                        exam.getExamName(),
                        courseName,
                        exam.getExamDate().toString(),
                        exam.getDurationMinutes() + " mins",
                        status
                });
            }
        }
    }

    private void loadMyResults() {
        resultsModel.setRowCount(0);
        List<Result> results = ResultService.listByStudent(currentUser.getUserId());
        if (results.isEmpty()) {
            return;
        }

        for (Result r : results) {
            Exam exam = ExamService.getExamById(r.getExamId());
            String examName = exam != null ? exam.getExamName() : "Unknown Exam";

            Course course = null;
            if (exam != null) {
                course = CourseService.getCourseById(exam.getCourseId());
            }
            String courseName = course != null ? course.getCourseName() : "Unknown Course";

            int totalMarks = exam != null ? ExamService.getTotalMarks(exam.getExamId()) : 0;
            double score = r.getScore();
            String scoreText = totalMarks > 0 ? String.format("%.1f/%d", score, totalMarks) : String.format("%.1f", score);

            double percentage = (totalMarks > 0) ? (score / totalMarks) * 100.0 : 0.0;
            String grade;
            if (percentage >= 80) grade = "A";
            else if (percentage >= 70) grade = "B";
            else if (percentage >= 60) grade = "C";
            else if (percentage >= 50) grade = "D";
            else grade = "F";

            String dateTaken = r.getDateTaken() != null ? r.getDateTaken().toLocalDate().toString() : "N/A";

            resultsModel.addRow(new Object[]{
                    examName,
                    courseName,
                    scoreText,
                    grade,
                    dateTaken,
                    "Completed"
            });
        }
    }

    private void loadSchedule() {
        scheduleModel.setRowCount(0);
        // Use the same logic as loadMyExams, but only show upcoming exams
        List<Enrollment> enrollments = EnrollmentService.getStudentEnrollments(currentUser.getUserId());
        if (enrollments.isEmpty()) {
            return;
        }

        java.time.LocalDate today = java.time.LocalDate.now();

        // Build teacher lookup for nicer instructor display
        java.util.List<User> teachers = UserService.getUsersByRole(User.Role.TEACHER);
        java.util.Map<String, String> teacherMap = new java.util.HashMap<>();
        for (User t : teachers) {
            teacherMap.put(t.getUserId(), t.getUsername());
        }

        for (Enrollment enrollment : enrollments) {
            int courseId = enrollment.getCourseId();
            Course course = CourseService.getCourseById(courseId);
            String courseName = course != null ? course.getCourseName() : "Unknown";

            List<Exam> exams = ExamService.getExamsByCourse(courseId);
            for (Exam exam : exams) {
                if (!exam.getExamDate().isBefore(today)) {
                    String teacherId = (course != null) ? course.getCreatedBy() : null;
                    String instructor;
                    if (teacherId != null && teacherMap.containsKey(teacherId)) {
                        instructor = teacherId + " - " + teacherMap.get(teacherId);
                    } else if (teacherId != null) {
                        instructor = teacherId;
                    } else {
                        instructor = "N/A";
                    }

                    scheduleModel.addRow(new Object[]{
                            exam.getExamName(),
                            courseName,
                            exam.getExamDate().toString(),
                            exam.getDurationMinutes() + " minutes",
                            instructor
                    });
                }
            }
        }
    }

    private void enrollInCourse(int selectedRow) {
        if (selectedRow >= 0) {
            int courseId = (int) coursesModel.getValueAt(selectedRow, 0);
            String courseName = (String) coursesModel.getValueAt(selectedRow, 1);

            // Check existing status first
            String currentStatus = (String) coursesModel.getValueAt(selectedRow, 4);
            if ("Enrolled".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "You are already enrolled in this course.",
                        "Already Enrolled",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            } else if ("Pending".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Your enrollment request for this course is already pending.",
                        "Request Pending",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "<html><b>Enroll in Course</b><br><br>" +
                            "Course: " + courseName + "<br>" +
                            "Are you sure you want to enroll?",
                    "Confirm Enrollment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                Enrollment enrollment = new Enrollment(currentUser.getUserId(), courseId);
                boolean ok = EnrollmentService.requestEnrollment(enrollment);
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Enrollment request submitted successfully.\n" +
                                    "Please wait for approval.",
                            "Request Submitted",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadAvailableCourses();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to submit enrollment request.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to enroll in",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void takeExam(int selectedRow) {
        if (selectedRow >= 0) {
            int examId = (int) examsModel.getValueAt(selectedRow, 0);
            String examName = (String) examsModel.getValueAt(selectedRow, 1);
            String status = (String) examsModel.getValueAt(selectedRow, 5);

            if (!"Available".equalsIgnoreCase(status) && !"Upcoming".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(this,
                        "This exam is not available for taking at the moment.\nStatus: " + status,
                        "Exam Not Available",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "<html><b>Start Exam</b><br><br>" +
                            "Exam: " + examName + "<br>" +
                            "Make sure you are ready before starting.<br>" +
                            "You cannot pause once started!",
                    "Confirm Exam Start",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                Exam exam = ExamService.getExamById(examId);
                if (exam == null) {
                    JOptionPane.showMessageDialog(this,
                            "Could not load exam details from database.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    new ExamAttemptFrame(currentUser, exam).setVisible(true);
                });
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select an exam to take",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void viewCourseDetails(int selectedRow) {
        if (selectedRow >= 0) {
            String courseName = (String) coursesModel.getValueAt(selectedRow, 1);
            String description = (String) coursesModel.getValueAt(selectedRow, 2);
            String instructor = (String) coursesModel.getValueAt(selectedRow, 3);

            JOptionPane.showMessageDialog(this,
                    "<html><b>Course Details</b><br><br>" +
                            "<b>Course:</b> " + courseName + "<br>" +
                            "<b>Instructor:</b> " + instructor + "<br>" +
                            "<b>Description:</b> " + description + "<br><br>" +
                            "Contact the instructor for more information.",
                    "Course Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showExamInstructions(int selectedRow) {
        if (selectedRow >= 0) {
            String examName = (String) examsModel.getValueAt(selectedRow, 1);
            String duration = (String) examsModel.getValueAt(selectedRow, 4);

            JOptionPane.showMessageDialog(this,
                    "<html><b>Exam Instructions</b><br><br>" +
                            "<b>Exam:</b> " + examName + "<br>" +
                            "<b>Duration:</b> " + duration + "<br><br>" +
                            "• Read all questions carefully<br>" +
                            "• Manage your time wisely<br>" +
                            "• No external help allowed<br>" +
                            "• Save your answers regularly<br>" +
                            "• Submit before time expires",
                    "Exam Instructions",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewResultDetails(int selectedRow) {
        if (selectedRow >= 0) {
            String examName = (String) resultsModel.getValueAt(selectedRow, 0);
            String score = (String) resultsModel.getValueAt(selectedRow, 2);
            String grade = (String) resultsModel.getValueAt(selectedRow, 3);

            JOptionPane.showMessageDialog(this,
                    "<html><b>Result Details</b><br><br>" +
                            "<b>Exam:</b> " + examName + "<br>" +
                            "<b>Score:</b> " + score + "<br>" +
                            "<b>Grade:</b> " + grade + "<br><br>" +
                            "Detailed feedback available from your instructor.",
                    "Result Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void downloadResults() {
        JOptionPane.showMessageDialog(this,
                "Download feature will be available soon!\n" +
                        "You'll be able to download your academic report in PDF format.",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this,
                "You have been logged out successfully.",
                "Logout Successful",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
package com.offlineexam.view;

import com.offlineexam.model.*;
import com.offlineexam.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.util.List;

public class TeacherDashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private DefaultTableModel coursesModel, examsModel, enrollmentsModel;

    public TeacherDashboard(User user) {
        this.currentUser = user;
        setTitle("Teacher Dashboard - Exam System");
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
                GradientPaint gradient = new GradientPaint(0, 0, new Color(39, 174, 96), 0, getHeight(), new Color(33, 145, 80));
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Teaching Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JLabel welcomeLabel = new JLabel("Welcome, Professor " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(127, 140, 141));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        // Create styled tabbed pane (tabs will be hidden, navigation uses sidebar)
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Style the tabs
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(44, 62, 80));

        tabbedPane.addTab("My Courses", createCoursesPanel());
        tabbedPane.addTab("Exam Management", createExamsPanel());
        tabbedPane.addTab("Enrollment Requests", createEnrollmentsPanel());
        tabbedPane.addTab("Settings", createSettingsPanel());

        // Hide the visible tab bar; navigation is handled by sidebar buttons
        tabbedPane.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int runCount, int maxTabHeight) {
                return 0;
            }
        });

        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo
        JLabel logoLabel = new JLabel("TEACHER PANEL");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Navigation buttons
        String[] menuItems = {"Overview", "Courses", "Exams", "Students", "Grades", "Settings"};

        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButton.addActionListener(e -> {
                String text = menuButton.getText();
                if (text.contains("Overview") || text.contains("Courses")) {
                    tabbedPane.setSelectedIndex(0); // My Courses
                } else if (text.contains("Exams") || text.contains("Grades")) {
                    tabbedPane.setSelectedIndex(1); // Exam Management
                } else if (text.contains("Students")) {
                    tabbedPane.setSelectedIndex(2); // Enrollment Requests
                } else if (text.contains("Settings")) {
                    tabbedPane.setSelectedIndex(3); // Settings
                }
            });
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        sidebar.add(Box.createVerticalGlue());

        // User info and logout
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(39, 174, 96));
        userPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        userPanel.setMaximumSize(new Dimension(250, 100));

        JLabel userName = new JLabel("Prof. " + currentUser.getFullName());
        userName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userName.setForeground(Color.WHITE);
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userRole = new JLabel("Teacher");
        userRole.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        userRole.setForeground(new Color(220, 220, 220));
        userRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutBtn = createMenuButton("Logout");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());

        userPanel.add(userName);
        userPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userPanel.add(userRole);
        userPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userPanel.add(logoutBtn);

        sidebar.add(userPanel);

        return sidebar;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(39, 174, 96));
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
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columns = {"Course ID", "Course Name", "Description", "Students", "Status", "Actions"};
        coursesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
            }
        };
        JTable coursesTable = createStyledTable(coursesModel);

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshBtn = createActionButton("Refresh Courses", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadCourses());

        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setOpaque(false);

        // Profile update panel
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("Profile"));
        profilePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField(currentUser.getUserId());
        userIdField.setEditable(false);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(currentUser.getUsername());

        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordField = new JPasswordField();

        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(userIdLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(userIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        profilePanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(passwordField, gbc);

        JButton updateProfileBtn = createActionButton("Update Profile", new Color(39, 174, 96));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        profilePanel.add(updateProfileBtn, gbc);

        updateProfileBtn.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty.");
                return;
            }

            if (newPassword.isEmpty()) {
                newPassword = currentUser.getPassword();
            }

            // fullName and email are not stored in DB, so pass nulls
            boolean ok = UserService.updateUserProfile(currentUser.getUserId(), newUsername, newPassword, null, null);
            if (ok) {
                currentUser.setUsername(newUsername);
                currentUser.setPassword(newPassword);
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile.");
            }
        });

        // Overview panel
        JPanel overviewPanel = new JPanel();
        overviewPanel.setBorder(BorderFactory.createTitledBorder("Overview"));
        overviewPanel.setLayout(new GridLayout(3, 1, 5, 5));
        overviewPanel.setOpaque(false);

        int totalCourses = 0;
        int totalExams = 0;
        int totalStudents = 0;

        List<Course> teacherCourses = CourseService.getCoursesByTeacher(currentUser.getUserId());
        totalCourses = teacherCourses.size();
        for (Course c : teacherCourses) {
            totalStudents += EnrollmentService.countApprovedEnrollmentsByCourse(c.getCourseId());
        }

        List<Exam> teacherExams = ExamService.getExamsByTeacher(currentUser.getUserId());
        totalExams = teacherExams.size();

        overviewPanel.add(new JLabel("Total Assigned Courses: " + totalCourses));
        overviewPanel.add(new JLabel("Total Exams Created: " + totalExams));
        overviewPanel.add(new JLabel("Total Students Enrolled: " + totalStudents));

        topPanel.add(profilePanel);
        topPanel.add(overviewPanel);

        // Assigned courses table
        String[] cols = {"Course ID", "Course Name", "Description", "Students"};
        DefaultTableModel assignedModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable assignedTable = createStyledTable(assignedModel);
        for (Course c : teacherCourses) {
            int students = EnrollmentService.countApprovedEnrollmentsByCourse(c.getCourseId());
            assignedModel.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getDescription(),
                    students
            });
        }

        JScrollPane tableScroll = new JScrollPane(assignedTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createExamsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Exam ID", "Exam Title", "Course", "Questions", "Duration", "Total Marks", "Status", "Actions"};
        examsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only actions column is editable
            }
        };
        JTable examsTable = createStyledTable(examsModel);

        examsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = examsTable.rowAtPoint(e.getPoint());
                int col = examsTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 7) {
                    handleManageExam(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(examsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton createExamBtn = createActionButton("Create New Exam", new Color(39, 174, 96));
        createExamBtn.addActionListener(e -> createExam());

        JButton viewResultsBtn = createActionButton("View Results", new Color(155, 89, 182));
        viewResultsBtn.addActionListener(e -> viewExamResults());

        JButton refreshBtn = createActionButton("Refresh", new Color(52, 152, 219));
        refreshBtn.addActionListener(e -> loadExams());

        buttonPanel.add(createExamBtn);
        buttonPanel.add(viewResultsBtn);
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEnrollmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Request ID", "Student", "Course", "Request Date", "Status"};
        enrollmentsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable enrollmentsTable = createStyledTable(enrollmentsModel);

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

    private JTable createStyledTable(DefaultTableModel model) {
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

        // Custom header renderer with solid green background and centered text
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerRenderer.setOpaque(true);
        headerRenderer.setBackground(new Color(39, 174, 96));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(32, 148, 82)));

        header.setDefaultRenderer(headerRenderer);

        return table;
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

    private void loadData() {
        loadCourses();
        loadExams();
        loadEnrollments();
    }

    private void loadCourses() {
        coursesModel.setRowCount(0);
        List<Course> courses = CourseService.getCoursesByTeacher(currentUser.getUserId());

        for (Course course : courses) {
            int studentCount = getStudentCountForCourse(course.getCourseId());
            String status = studentCount > 0 ? "Active" : "No Students";

            coursesModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getDescription(),
                    studentCount + " students",
                    status,
                    "Manage"
            });
        }
    }

    private void handleManageExam(int row) {
        if (row < 0) {
            return;
        }

        int examId = (int) examsModel.getValueAt(row, 0);
        Exam exam = ExamService.getExamById(examId);
        if (exam == null) {
            JOptionPane.showMessageDialog(this,
                    "Could not load exam details from database.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] options = {"Add / Manage Questions", "Edit Exam", "Delete Exam", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "What would you like to do with exam '" + exam.getExamName() + "'?",
                "Manage Exam",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            EnhancedQuestionEditorFrame editor = new EnhancedQuestionEditorFrame(exam, currentUser);
            editor.setVisible(true);
        } else if (choice == 1) {
            editExam(exam);
        } else if (choice == 2) {
            deleteExam(exam);
        }
    }

    private void editExam(Exam exam) {
        String newTitle = JOptionPane.showInputDialog(this,
                "Exam title:", exam.getExamName());
        if (newTitle == null) return;
        newTitle = newTitle.trim();
        if (newTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.");
            return;
        }

        String newDurationStr = JOptionPane.showInputDialog(this,
                "Duration in minutes:", exam.getDurationMinutes());
        if (newDurationStr == null) return;
        int newDuration;
        try {
            newDuration = Integer.parseInt(newDurationStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid duration.");
            return;
        }

        exam.setExamName(newTitle);
        exam.setDurationMinutes(newDuration);

        boolean ok = ExamService.updateExam(exam);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Exam updated successfully.");
            loadExams();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update exam.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExam(Exam exam) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete exam '" + exam.getExamName() + "' and all its questions?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = ExamService.deleteExam(exam.getExamId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Exam deleted successfully.");
            loadExams();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete exam.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExams() {
        examsModel.setRowCount(0);
        List<Exam> exams = ExamService.getExamsByTeacher(currentUser.getUserId());

        for (Exam exam : exams) {
            Course course = CourseService.getCourseById(exam.getCourseId());
            String courseName = course != null ? course.getCourseName() : "Unknown Course";
            int questionCount = ExamService.getQuestionCount(exam.getExamId());
            double totalMarks = ExamService.getTotalMarks(exam.getExamId());
            String status = exam.getExamDate().isAfter(java.time.LocalDate.now()) ? "Upcoming" : "Completed";

            examsModel.addRow(new Object[]{
                    exam.getExamId(),
                    exam.getExamName(),
                    courseName,
                    questionCount + " questions",
                    exam.getDurationMinutes() + " mins",
                    totalMarks + " marks",
                    status,
                    "Manage"
            });
        }
    }

    private void loadEnrollments() {
        enrollmentsModel.setRowCount(0);
        List<Enrollment> pending = EnrollmentService.getPendingEnrollmentsByTeacher(currentUser.getUserId());

        for (Enrollment e : pending) {
            User student = UserService.getUserById(e.getStudentId());
            String studentLabel = (student != null)
                    ? student.getUserId() + " - " + student.getUsername()
                    : e.getStudentId();

            Course course = CourseService.getCourseById(e.getCourseId());
            String courseLabel = (course != null)
                    ? course.getCourseId() + " - " + course.getCourseName()
                    : String.valueOf(e.getCourseId());

            String date = e.getRequestDate() != null
                    ? e.getRequestDate().toString()
                    : "N/A";

            enrollmentsModel.addRow(new Object[]{
                    e.getEnrollmentId(),
                    studentLabel,
                    courseLabel,
                    date,
                    e.getStatus()
            });
        }
    }

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
                loadCourses(); // update student counts/statuses
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update enrollment request.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getStudentCountForCourse(int courseId) {
        return EnrollmentService.countApprovedEnrollmentsByCourse(courseId);
    }

    private void createExam() {
        // Open the enhanced exam creator
        new ExamCreatorFrame(currentUser).setVisible(true);
    }

    private void viewExamResults() {
        JOptionPane.showMessageDialog(this,
                "Exam results functionality will be implemented here.\n" +
                        "This will show student performance and statistics for each exam.",
                "Exam Results", JOptionPane.INFORMATION_MESSAGE);
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
            User testUser = new User("teacher1", "password", "Professor Smith", "prof.smith@school.edu", User.Role.TEACHER);
            new TeacherDashboard(testUser).setVisible(true);
        });
    }
}
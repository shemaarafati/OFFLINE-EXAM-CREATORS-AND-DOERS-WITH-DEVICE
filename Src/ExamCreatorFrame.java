package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.model.Course;
import com.offlineexam.model.Exam;
import com.offlineexam.service.CourseService;
import com.offlineexam.service.ExamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ExamCreatorFrame extends JFrame {
    private User teacher;
    private JComboBox<Course> courseCombo;
    private JTextField titleField;
    private JTextField dateField;
    private JSpinner durationSpinner;
    private JSpinner marksSpinner;
    private JButton createBtn;
    private JButton cancelBtn;

    // Colors
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(51, 51, 51);
    private final Color TEXT_SECONDARY = new Color(108, 117, 125);

    public ExamCreatorFrame(User teacher) {
        this.teacher = teacher;
        initializeFrame();
        initComponents();
        loadTeacherCourses();
    }

    private void initializeFrame() {
        setTitle("Create New Exam - Offline Exam System");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            setIconImage(createSimpleIcon());
        } catch (Exception e) {
            System.out.println("Icon creation failed: " + e.getMessage());
        }
    }

    private Image createSimpleIcon() {
        int size = 32;
        BufferedImage icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillOval(2, 2, size-4, size-4);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("E", size/2-4, size/2+4);
        g2d.dispose();
        return icon;
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Create New Exam");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Fill in the exam details below");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JLabel teacherLabel = new JLabel("Teacher: " + teacher.getFullName());
        teacherLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        teacherLabel.setForeground(TEXT_SECONDARY);
        teacherLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(teacherLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Course Selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createFormLabel("Select Course *"), gbc);

        gbc.gridx = 1;
        courseCombo = createStyledComboBox();
        formPanel.add(courseCombo, gbc);

        gbc.gridx = 2;
        JButton refreshBtn = createIconButton("🔄", "Refresh Courses");
        refreshBtn.addActionListener(e -> refreshCourses());
        formPanel.add(refreshBtn, gbc);

        // Exam Title
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createFormLabel("Exam Title *"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        titleField = createFormTextField();
        titleField.setToolTipText("Enter a descriptive title for the exam");
        formPanel.add(titleField, gbc);

        // Exam Date
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(createFormLabel("Exam Date *"), gbc);

        gbc.gridx = 1;
        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.setBackground(CARD_COLOR);
        dateField = createFormTextField();
        dateField.setText(LocalDate.now().toString());
        dateField.setToolTipText("YYYY-MM-DD format");
        datePanel.add(dateField, BorderLayout.CENTER);

        JButton datePickerBtn = createIconButton("📅", "Select Date");
        datePickerBtn.addActionListener(e -> showEnhancedDatePicker());
        datePanel.add(datePickerBtn, BorderLayout.EAST);

        formPanel.add(datePanel, gbc);

        gbc.gridx = 2;
        JButton todayBtn = createIconButton("⏰", "Set to Today");
        todayBtn.addActionListener(e -> setTodayDate());
        formPanel.add(todayBtn, gbc);

        // Duration
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createFormLabel("Duration (minutes) *"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        durationSpinner = createStyledSpinner(60, 15, 240, 5);
        durationSpinner.setToolTipText("Exam duration in minutes (15-240)");
        formPanel.add(durationSpinner, gbc);

        // Total Marks
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createFormLabel("Total Marks *"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        marksSpinner = createStyledSpinner(100, 10, 500, 10);
        marksSpinner.setToolTipText("Total marks for the exam (10-500)");
        formPanel.add(marksSpinner, gbc);

        // Info panel
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        formPanel.add(createInfoPanel(), gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        cancelBtn = createStyledButton("Cancel", DANGER_COLOR);
        cancelBtn.addActionListener(e -> handleCancel());

        createBtn = createStyledButton("Create Exam & Add Questions", SUCCESS_COLOR);
        createBtn.addActionListener(e -> createExam());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(createBtn);

        return buttonPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(232, 244, 253));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(133, 193, 233), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel infoLabel = new JLabel(
                "<html><b>💡 Exam Creation Tips:</b><br>" +
                        "• Choose the correct course for the exam<br>" +
                        "• Use a clear and descriptive exam title<br>" +
                        "• Set appropriate duration based on exam complexity<br>" +
                        "• Ensure total marks match your question plan<br>" +
                        "• You'll be able to add questions after creating the exam</html>"
        );
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(49, 112, 143));

        infoPanel.add(infoLabel, BorderLayout.CENTER);
        return infoPanel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JTextField createFormTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(CARD_COLOR);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(SECONDARY_COLOR, 2),
                        new EmptyBorder(9, 11, 9, 11)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(206, 212, 218), 1),
                        new EmptyBorder(10, 12, 10, 12)
                ));
            }
        });

        return field;
    }

    private JComboBox<Course> createStyledComboBox() {
        JComboBox<Course> combo = new JComboBox<Course>() {
            @Override
            public void setPopupVisible(boolean v) {
                if (v && getItemCount() == 0) {
                    JOptionPane.showMessageDialog(this,
                            "No courses available. Please create a course first.",
                            "No Courses", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                super.setPopupVisible(v);
            }
        };

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Course) {
                    Course course = (Course) value;
                    setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
                }
                return c;
            }
        });

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(CARD_COLOR);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(8, 8, 8, 8)
        ));

        return combo;
    }

    private JSpinner createStyledSpinner(int value, int min, int max, int step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(CARD_COLOR);
        editor.getTextField().setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(8, 8, 8, 8)
        ));

        return spinner;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (!isEnabled()) {
                    g2.setColor(color.darker().darker());
                } else if (getModel().isPressed()) {
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 45));

        return button;
    }

    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setToolTipText(tooltip);
        button.setBackground(CARD_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(206, 212, 218), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(241, 243, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(CARD_COLOR);
            }
        });

        return button;
    }

    private void loadTeacherCourses() {
        SwingWorker<List<Course>, Void> worker = new SwingWorker<List<Course>, Void>() {
            @Override
            protected List<Course> doInBackground() throws Exception {
                System.out.println("Loading courses for teacher ID: " + teacher.getUserId());
                List<Course> courses = CourseService.getCoursesByTeacher(teacher.getUserId());
                System.out.println("Found " + courses.size() + " courses");
                return courses;
            }

            @Override
            protected void done() {
                try {
                    List<Course> courses = get();
                    courseCombo.removeAllItems();

                    if (courses.isEmpty()) {
                        Course dummyCourse = new Course(0, "No courses available", "N/A", "Please create a course first");
                        courseCombo.addItem(dummyCourse);
                        courseCombo.setEnabled(false);
                        createBtn.setEnabled(false);
                        System.out.println("No courses available for teacher");
                    } else {
                        for (Course course : courses) {
                            courseCombo.addItem(course);
                            System.out.println("Loaded course: " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
                        }
                        courseCombo.setEnabled(true);
                        createBtn.setEnabled(true);
                        System.out.println("Successfully loaded " + courses.size() + " courses");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading courses: " + e.getMessage());
                    e.printStackTrace();
                    showError("Failed to load courses: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showEnhancedDatePicker() {
        String[] days = new String[31];
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String[] years = new String[11];

        LocalDate now = LocalDate.now();
        for (int i = 0; i < 31; i++) days[i] = String.valueOf(i + 1);
        for (int i = 0; i < 11; i++) years[i] = String.valueOf(now.getYear() + i - 5);

        JComboBox<String> dayCombo = new JComboBox<>(days);
        JComboBox<String> monthCombo = new JComboBox<>(months);
        JComboBox<String> yearCombo = new JComboBox<>(years);

        dayCombo.setSelectedIndex(now.getDayOfMonth() - 1);
        monthCombo.setSelectedIndex(now.getMonthValue() - 1);
        yearCombo.setSelectedItem(String.valueOf(now.getYear()));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Day:"));
        panel.add(dayCombo);
        panel.add(new JLabel("Month:"));
        panel.add(monthCombo);
        panel.add(new JLabel("Year:"));
        panel.add(yearCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Select Exam Date",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int day = Integer.parseInt((String) dayCombo.getSelectedItem());
                int month = monthCombo.getSelectedIndex() + 1;
                int year = Integer.parseInt((String) yearCombo.getSelectedItem());

                LocalDate selectedDate = LocalDate.of(year, month, day);
                dateField.setText(selectedDate.toString());
            } catch (Exception e) {
                showError("Invalid date selected");
            }
        }
    }

    private void setTodayDate() {
        dateField.setText(LocalDate.now().toString());
    }

    private void refreshCourses() {
        loadTeacherCourses();
        showSuccess("Courses list refreshed successfully");
    }

    private void handleCancel() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel exam creation?\nAny unsaved data will be lost.",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void createExam() {
        if (!validateInputs()) {
            return;
        }

        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        String examTitle = titleField.getText().trim();
        LocalDate examDate = LocalDate.parse(dateField.getText().trim());
        int duration = (int) durationSpinner.getValue();
        int totalMarks = (int) marksSpinner.getValue();

        // Debug: Print exam details before creation
        System.out.println("=== Creating Exam ===");
        System.out.println("Course ID: " + selectedCourse.getCourseId());
        System.out.println("Course Name: " + selectedCourse.getCourseName());
        System.out.println("Title: " + examTitle);
        System.out.println("Date: " + examDate);
        System.out.println("Duration: " + duration);
        System.out.println("Total Marks: " + totalMarks);
        System.out.println("Teacher ID: " + teacher.getUserId());

        Exam exam = new Exam(
                selectedCourse.getCourseId(),
                examTitle,
                examDate,
                duration,
                totalMarks,
                teacher.getUserId()
        );

        createBtn.setEnabled(false);
        createBtn.setText("Creating Exam...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return createExamWithFallback(exam);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        System.out.println("✅ Exam created successfully!");
                        showExamCreatedSuccess(exam, selectedCourse);
                        dispose();
                    } else {
                        System.err.println("❌ Failed to create exam - service returned false");
                        showDetailedError(
                                "Failed to create exam. Possible reasons:\n\n" +
                                        "• Database connection issue\n" +
                                        "• Course ID " + selectedCourse.getCourseId() + " might not exist\n" +
                                        "• Duplicate exam title\n" +
                                        "• Invalid data format\n" +
                                        "• Server-side error\n\n" +
                                        "Please check console for detailed error messages."
                        );
                        createBtn.setEnabled(true);
                        createBtn.setText("Create Exam & Add Questions");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Exception in exam creation: " + e.getMessage());
                    e.printStackTrace();
                    showError("Error creating exam: " + e.getMessage());
                    createBtn.setEnabled(true);
                    createBtn.setText("Create Exam & Add Questions");
                }
            }
        };
        worker.execute();
    }

    private boolean createExamWithFallback(Exam exam) {
        try {
            System.out.println("Attempting to create exam via ExamService...");
            boolean result = ExamService.createExamWithTotalMarks(exam);
            System.out.println("ExamService.createExamWithTotalMarks returned: " + result);

            if (!result) {
                // Try alternative method if available
                System.out.println("Trying alternative creation method...");
                // Uncomment if you have an alternative method:
                // result = ExamService.createExam(exam);
                // System.out.println("Alternative method returned: " + result);
            }

            return result;
        } catch (Exception e) {
            System.err.println("❌ Exception in createExamWithFallback: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateInputs() {
        // Check course selection
        if (courseCombo.getSelectedItem() == null) {
            showWarning("Please select a course for the exam.");
            courseCombo.requestFocus();
            return false;
        }

        Course selectedCourse = (Course) courseCombo.getSelectedItem();

        if (selectedCourse.getCourseId() == 0) {
            showWarning("No courses available. Please create a course first.");
            courseCombo.requestFocus();
            return false;
        }

        // Check exam title
        String examTitle = titleField.getText().trim();
        if (examTitle.isEmpty()) {
            showWarning("Please enter an exam title.");
            titleField.requestFocus();
            return false;
        }

        if (examTitle.length() < 3) {
            showWarning("Exam title should be at least 3 characters long.");
            titleField.requestFocus();
            return false;
        }

        // Check date
        String dateText = dateField.getText().trim();
        if (dateText.isEmpty()) {
            showWarning("Please select an exam date.");
            return false;
        }

        try {
            LocalDate examDate = LocalDate.parse(dateText);
            if (examDate.isBefore(LocalDate.now())) {
                showWarning("Exam date cannot be in the past. Please select a future date.");
                return false;
            }
        } catch (DateTimeParseException e) {
            showWarning("Invalid date format. Please use YYYY-MM-DD format.\nExample: " + LocalDate.now());
            return false;
        }

        return true;
    }

    private void showExamCreatedSuccess(Exam exam, Course course) {
        String message = String.format(
                "<html><div style='text-align: center;'>" +
                        "<h3 style='color: #27ae60;'>✅ Exam Created Successfully!</h3>" +
                        "<div style='text-align: left; margin: 15px;'>" +
                        "<b>Exam Details:</b><br>" +
                        "• <b>Course:</b> %s<br>" +
                        "• <b>Title:</b> %s<br>" +
                        "• <b>Date:</b> %s<br>" +
                        "• <b>Duration:</b> %d minutes<br>" +
                        "• <b>Total Marks:</b> %d<br>" +
                        "</div>" +
                        "You can now add questions to the exam." +
                        "</div></html>",
                course.getCourseName(), exam.getExamName(),
                exam.getExamDate(), exam.getDurationMinutes(), exam.getTotalMarks()
        );

        JOptionPane.showMessageDialog(this, message, "Exam Created", JOptionPane.INFORMATION_MESSAGE);

        SwingUtilities.invokeLater(() -> {
            EnhancedQuestionEditorFrame editor = new EnhancedQuestionEditorFrame(exam, teacher);
            editor.setVisible(true);
        });
    }

    private void showDetailedError(String message) {
        JOptionPane.showMessageDialog(this, message, "Creation Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(systemLookAndFeel);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception exc) {
                    System.err.println("Failed to set look and feel: " + exc.getMessage());
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            User testTeacher = new User("teacher1", "password",
                    "Professor John Smith", "prof.smith@university.edu", User.Role.TEACHER);

            // Set user ID using reflection if needed
            try {
                java.lang.reflect.Field userIdField = User.class.getDeclaredField("userId");
                userIdField.setAccessible(true);
                userIdField.set(testTeacher, 1);
            } catch (Exception e) {
                System.err.println("Could not set user ID: " + e.getMessage());
            }

            ExamCreatorFrame frame = new ExamCreatorFrame(testTeacher);
            frame.setVisible(true);
        });
    }
}

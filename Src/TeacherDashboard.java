package com.offlineexam.view;

import com.offlineexam.model.*;
import com.offlineexam.service.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TeacherDashboard extends JFrame {
    private User teacher;

    public TeacherDashboard(User teacher) {
        this.teacher = teacher;
        setTitle("Teacher Dashboard - " + teacher.getFullName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        // Top panel with welcome message
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(70, 130, 180));

        JLabel welcomeLabel = new JLabel("Teacher Dashboard - Welcome, " + teacher.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh All");
        btnRefresh.setBackground(new Color(34, 139, 34));
        btnRefresh.setForeground(Color.WHITE);
        topPanel.add(btnRefresh, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Main tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));

        // Tab 1: Enrollment Requests
        JPanel requestsPanel = createRequestsPanel();
        tabbedPane.addTab("Enrollment Requests", requestsPanel);

        // Tab 2: My Courses
        JPanel coursesPanel = createCoursesPanel();
        tabbedPane.addTab("My Courses", coursesPanel);

        // Tab 3: Exam Management
        JPanel examsPanel = createExamsPanel();
        tabbedPane.addTab("Exam Management", examsPanel);

        // Tab 4: Results
        JPanel resultsPanel = createResultsPanel();
        tabbedPane.addTab("Student Results", resultsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Refresh button action
        btnRefresh.addActionListener(e -> {
            refreshAllTabs();
            statusLabel.setText("All data refreshed - " + new java.util.Date());
        });

        // Load initial data
        refreshAllTabs();
    }

    private JPanel createRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Pending Enrollment Requests");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Requests list
        DefaultListModel<String> requestsModel = new DefaultListModel<>();
        JList<String> requestsList = new JList<>(requestsModel);
        requestsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsList.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(requestsList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnApprove = new JButton("Approve Selected");
        btnApprove.setBackground(new Color(34, 139, 34));
        btnApprove.setForeground(Color.WHITE);

        JButton btnViewAll = new JButton("View All Requests");
        JButton btnRefresh = new JButton("Refresh Requests");

        buttonPanel.add(btnApprove);
        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnRefresh);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        btnApprove.addActionListener(e -> {
            int selectedIndex = requestsList.getSelectedIndex();
            if (selectedIndex != -1) {
                approveSelectedRequest(selectedIndex, requestsModel);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a request first");
            }
        });

        btnViewAll.addActionListener(e -> showAllRequests());

        btnRefresh.addActionListener(e -> loadPendingRequests(requestsModel));

        // Load initial data
        loadPendingRequests(requestsModel);

        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("My Assigned Courses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> coursesModel = new DefaultListModel<>();
        JList<String> coursesList = new JList<>(coursesModel);
        coursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(coursesList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnCreateExam = new JButton("Create Exam for Selected Course");
        btnCreateExam.setBackground(new Color(70, 130, 180));
        btnCreateExam.setForeground(Color.WHITE);

        JButton btnRefresh = new JButton("Refresh Courses");

        buttonPanel.add(btnCreateExam);
        buttonPanel.add(btnRefresh);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnCreateExam.addActionListener(e -> {
            int selectedIndex = coursesList.getSelectedIndex();
            if (selectedIndex != -1) {
                createExamForCourse(selectedIndex, coursesModel);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course first");
            }
        });

        btnRefresh.addActionListener(e -> loadTeacherCourses(coursesModel));

        loadTeacherCourses(coursesModel);

        return panel;
    }

    private JPanel createExamsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("My Created Exams");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> examsModel = new DefaultListModel<>();
        JList<String> examsList = new JList<>(examsModel);
        examsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(examsList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnAddQuestions = new JButton("Add/Edit Questions");
        btnAddQuestions.setBackground(new Color(218, 165, 32));
        btnAddQuestions.setForeground(Color.WHITE);

        JButton btnViewResults = new JButton("View Exam Results");
        JButton btnRefresh = new JButton("Refresh Exams");

        buttonPanel.add(btnAddQuestions);
        buttonPanel.add(btnViewResults);
        buttonPanel.add(btnRefresh);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnAddQuestions.addActionListener(e -> {
            int selectedIndex = examsList.getSelectedIndex();
            if (selectedIndex != -1) {
                addQuestionsToExam(selectedIndex, examsModel);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an exam first");
            }
        });

        btnViewResults.addActionListener(e -> viewExamResults());

        btnRefresh.addActionListener(e -> loadTeacherExams(examsModel));

        loadTeacherExams(examsModel);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Student Results for My Courses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> resultsModel = new DefaultListModel<>();
        JList<String> resultsList = new JList<>(resultsModel);

        JScrollPane scrollPane = new JScrollPane(resultsList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnRefresh = new JButton("Refresh Results");
        JButton btnExport = new JButton("Export to CSV");
        btnExport.setBackground(new Color(139, 0, 0));
        btnExport.setForeground(Color.WHITE);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnExport);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadStudentResults(resultsModel));

        btnExport.addActionListener(e -> exportResultsToCSV());

        loadStudentResults(resultsModel);

        return panel;
    }

    private void refreshAllTabs() {
        // This would refresh all tabs - for now we'll just show a message
        JOptionPane.showMessageDialog(this, "All data refreshed successfully!");
    }

    private void loadPendingRequests(DefaultListModel<String> model) {
        model.clear();
        List<EnrollmentRequest> requests = RequestService.listPendingForTeacher(teacher.getFullName());

        if (requests.isEmpty()) {
            model.addElement("No pending enrollment requests");
            return;
        }

        for (EnrollmentRequest request : requests) {
            model.addElement(String.format("Student: %-20s | Course: %-15s | Submitted",
                    request.getStudent(), request.getCourse()));
        }
    }

    private void loadTeacherCourses(DefaultListModel<String> model) {
        model.clear();
        List<Course> allCourses = CourseService.listAll();

        boolean hasCourses = false;
        for (Course course : allCourses) {
            if (course.getTeacher() != null && course.getTeacher().getId() == teacher.getId()) {
                model.addElement(String.format("ID: %-3d | %-25s | %s",
                        course.getId(), course.getName(),
                        course.getDescription().length() > 30 ?
                                course.getDescription().substring(0, 30) + "..." : course.getDescription()));
                hasCourses = true;
            }
        }

        if (!hasCourses) {
            model.addElement("No courses assigned to you. Contact administrator.");
        }
    }

    private void loadTeacherExams(DefaultListModel<String> model) {
        model.clear();
        List<Exam> allExams = ExamService.listAll();

        boolean hasExams = false;
        for (Exam exam : allExams) {
            Course course = CourseService.findById(exam.getCourseId());
            if (course != null && course.getTeacher() != null && course.getTeacher().getId() == teacher.getId()) {
                int questionCount = exam.getQuestions().size();
                String status = questionCount > 0 ? "Ready" : "No Questions";
                model.addElement(String.format("ID: %-3d | %-20s | Questions: %-2d | %s",
                        exam.getId(), exam.getTitle(), questionCount, status));
                hasExams = true;
            }
        }

        if (!hasExams) {
            model.addElement("No exams created for your courses yet");
        }
    }

    private void loadStudentResults(DefaultListModel<String> model) {
        model.clear();
        List<Result> allResults = ResultService.listAll();

        boolean hasResults = false;
        for (Result result : allResults) {
            Exam exam = ExamService.findById(result.getExamId());
            if (exam != null) {
                Course course = CourseService.findById(exam.getCourseId());
                if (course != null && course.getTeacher() != null && course.getTeacher().getId() == teacher.getId()) {
                    User student = UserService.findById(result.getStudentId());
                    String studentName = student != null ? student.getFullName() : "Unknown Student";
                    String grade = getGrade(result.getScore());
                    model.addElement(String.format("%-20s | %-15s | Score: %-5.1f | Grade: %s",
                            studentName, exam.getTitle(), result.getScore(), grade));
                    hasResults = true;
                }
            }
        }

        if (!hasResults) {
            model.addElement("No results available for your courses");
        }
    }

    private String getGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }

    private void approveSelectedRequest(int index, DefaultListModel<String> model) {
        List<EnrollmentRequest> requests = RequestService.listPendingForTeacher(teacher.getFullName());

        if (index < requests.size()) {
            EnrollmentRequest request = requests.get(index);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Approve enrollment request from " + request.getStudent() + " for " + request.getCourse() + "?",
                    "Confirm Approval",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                RequestService.approveRequest(request);
                JOptionPane.showMessageDialog(this,
                        "Request approved for " + request.getStudent() + " in " + request.getCourse());
                loadPendingRequests(model); // Refresh the list
            }
        }
    }

    private void showAllRequests() {
        List<EnrollmentRequest> allRequests = RequestService.getAllRequests();

        if (allRequests.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No enrollment requests found");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("All Enrollment Requests:\n\n");

        for (EnrollmentRequest request : allRequests) {
            sb.append(String.format("Student: %s\nCourse: %s\nTeacher: %s\nStatus: %s\n\n",
                    request.getStudent(), request.getCourse(), request.getTeacher(), request.getStatus()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "All Enrollment Requests", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createExamForCourse(int index, DefaultListModel<String> model) {
        List<Course> allCourses = CourseService.listAll();
        List<Course> myCourses = new ArrayList<>();

        for (Course course : allCourses) {
            if (course.getTeacher() != null && course.getTeacher().getId() == teacher.getId()) {
                myCourses.add(course);
            }
        }

        if (index < myCourses.size()) {
            Course selectedCourse = myCourses.get(index);
            new ExamCreatorFrame(teacher).setVisible(true);
        }
    }

    private void addQuestionsToExam(int index, DefaultListModel<String> model) {
        List<Exam> allExams = ExamService.listAll();
        List<Exam> myExams = new ArrayList<>();

        for (Exam exam : allExams) {
            Course course = CourseService.findById(exam.getCourseId());
            if (course != null && course.getTeacher() != null && course.getTeacher().getId() == teacher.getId()) {
                myExams.add(exam);
            }
        }

        if (index < myExams.size()) {
            Exam selectedExam = myExams.get(index);
            new QuestionEditorFrame(selectedExam).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid exam");
        }
    }

    private void viewExamResults() {
        List<Result> allResults = ResultService.listAll();

        StringBuilder resultsText = new StringBuilder();
        resultsText.append("EXAM RESULTS SUMMARY\n");
        resultsText.append("====================\n\n");

        boolean hasResults = false;

        // Group by exam
        for (Exam exam : ExamService.listAll()) {
            Course course = CourseService.findById(exam.getCourseId());
            if (course != null && course.getTeacher() != null && course.getTeacher().getId() == teacher.getId()) {
                resultsText.append("Exam: ").append(exam.getTitle()).append("\n");
                resultsText.append("Course: ").append(course.getName()).append("\n");
                resultsText.append("----------------------------------------\n");

                int resultCount = 0;
                double totalScore = 0;

                for (Result result : allResults) {
                    if (result.getExamId() == exam.getId()) {
                        User student = UserService.findById(result.getStudentId());
                        String studentName = student != null ? student.getFullName() : "Unknown";
                        resultsText.append(String.format("  %-20s: %6.2f\n", studentName, result.getScore()));
                        totalScore += result.getScore();
                        resultCount++;
                        hasResults = true;
                    }
                }

                if (resultCount > 0) {
                    double average = totalScore / resultCount;
                    resultsText.append(String.format("  Average Score: %.2f\n", average));
                } else {
                    resultsText.append("  No results yet\n");
                }
                resultsText.append("\n");
            }
        }

        if (!hasResults) {
            resultsText.append("No exam results available for your courses");
        }

        JTextArea textArea = new JTextArea(resultsText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Exam Results Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportResultsToCSV() {
        JOptionPane.showMessageDialog(this,
                "CSV export functionality would be implemented here.\n" +
                        "This would export all student results to a CSV file for further analysis.");
    }

    public static void main(String[] args) {
        // Test method - remove in production
        SwingUtilities.invokeLater(() -> {
            User testTeacher = new User(1, "John Doe", "teacher1", "pass", "teacher", "teacher@school.edu");
            new TeacherDashboard(testTeacher).setVisible(true);
        });
    }
}

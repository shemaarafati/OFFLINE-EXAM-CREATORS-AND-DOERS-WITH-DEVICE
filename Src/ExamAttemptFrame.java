package com.offlineexam.view;

import com.offlineexam.model.*;
import com.offlineexam.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class ExamAttemptFrame extends JFrame {
    private Exam exam;
    private User student;
    private List<Question> questions;
    private Map<Integer, String> studentAnswers;
    private Timer examTimer;
    private int timeRemaining;
    private JLabel timerLabel;

    public ExamAttemptFrame(User student, Exam exam) {
        this.exam = exam;
        this.student = student;
        this.studentAnswers = new HashMap<>();
        this.timeRemaining = exam.getDurationMinutes() * 60; // Convert to seconds
        this.questions = ExamService.getQuestionsByExam(exam.getExamId());
        if (this.questions != null && !this.questions.isEmpty()) {
            Collections.shuffle(this.questions);
        }

        setTitle("Attempting: " + exam.getExamName());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initComponents();
        startTimer();
    }

    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with exam info and timer
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Questions panel
        JPanel questionsPanel = createQuestionsPanel();
        mainPanel.add(new JScrollPane(questionsPanel), BorderLayout.CENTER);

        // Submit panel
        JPanel submitPanel = createSubmitPanel();
        mainPanel.add(submitPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add window listener to prevent accidental closure
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Exam info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        JLabel examNameLabel = new JLabel(exam.getExamName());
        examNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        examNameLabel.setForeground(Color.WHITE);

        Course course = CourseService.getCourseById(exam.getCourseId());
        String courseName = course != null ? course.getCourseName() : "Unknown Course";
        JLabel courseLabel = new JLabel("Course: " + courseName + " | Questions: " + questions.size());
        courseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        courseLabel.setForeground(new Color(200, 200, 200));

        infoPanel.add(examNameLabel);
        infoPanel.add(courseLabel);

        // Timer panel
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        timerPanel.setOpaque(false);

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timerLabel.setForeground(Color.WHITE);
        updateTimerDisplay();

        timerPanel.add(new JLabel("⏰ "));
        timerPanel.add(timerLabel);

        panel.add(infoPanel, BorderLayout.WEST);
        panel.add(timerPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createQuestionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        if (questions.isEmpty()) {
            JLabel noQuestionsLabel = new JLabel("No questions available for this exam.");
            noQuestionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noQuestionsLabel.setForeground(Color.RED);
            noQuestionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noQuestionsLabel);
        } else {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                panel.add(createQuestionPanel(question, i + 1));
                panel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }

        return panel;
    }

    private JPanel createQuestionPanel(Question question, int questionNumber) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);

        // Question header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel questionNumberLabel = new JLabel("Question " + questionNumber + " (" + question.getMarks() + " marks)");
        questionNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        questionNumberLabel.setForeground(new Color(52, 152, 219));

        JLabel typeLabel = new JLabel(question.getQuestionType() != null ? question.getQuestionType() : "MCQ");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(new Color(127, 140, 141));

        headerPanel.add(questionNumberLabel, BorderLayout.WEST);
        headerPanel.add(typeLabel, BorderLayout.EAST);

        // Question text
        JTextArea questionTextArea = new JTextArea(question.getQuestionText());
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        questionTextArea.setBackground(Color.WHITE);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        // Answer area
        JPanel answerPanel = createAnswerPanel(question);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(questionTextArea, BorderLayout.CENTER);
        panel.add(answerPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAnswerPanel(Question question) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String questionType = question.getQuestionType();
        if (questionType == null) {
            questionType = "MCQ"; // Default to MCQ if type is null
        }

        switch (questionType) {
            case "MCQ":
                panel.add(createMCQAnswerPanel(question), BorderLayout.CENTER);
                break;
            case "TRUE_FALSE":
                panel.add(createTrueFalseAnswerPanel(question), BorderLayout.CENTER);
                break;
            case "ESSAY":
                panel.add(createEssayAnswerPanel(question), BorderLayout.CENTER);
                break;
            default:
                panel.add(createMCQAnswerPanel(question), BorderLayout.CENTER); // Default fallback
        }

        return panel;
    }

    private JPanel createMCQAnswerPanel(Question question) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setOpaque(false);

        ButtonGroup bg = new ButtonGroup();

        // Create options array from the question fields
        String[] options = {
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD()
        };

        for (int i = 0; i < options.length; i++) {
            if (options[i] != null && !options[i].trim().isEmpty()) {
                JRadioButton rb = new JRadioButton((char)('A' + i) + ". " + options[i]);
                rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                rb.setActionCommand(String.valueOf((char)('A' + i))); // Store option letter
                rb.addActionListener(e -> studentAnswers.put(question.getQuestionId(), rb.getActionCommand()));
                bg.add(rb);
                panel.add(rb);
            }
        }

        return panel;
    }

    private JPanel createTrueFalseAnswerPanel(Question question) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton trueBtn = new JRadioButton("True");
        JRadioButton falseBtn = new JRadioButton("False");

        trueBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        falseBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        trueBtn.setActionCommand("True");
        falseBtn.setActionCommand("False");

        trueBtn.addActionListener(e -> studentAnswers.put(question.getQuestionId(), "True"));
        falseBtn.addActionListener(e -> studentAnswers.put(question.getQuestionId(), "False"));

        bg.add(trueBtn);
        bg.add(falseBtn);

        panel.add(trueBtn);
        panel.add(falseBtn);

        return panel;
    }

    private JPanel createEssayAnswerPanel(Question question) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JTextArea answerArea = new JTextArea(4, 40);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        answerArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        answerArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateAnswer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateAnswer(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateAnswer(); }

            private void updateAnswer() {
                studentAnswers.put(question.getQuestionId(), answerArea.getText());
            }
        });

        panel.add(new JScrollPane(answerArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSubmitPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JButton submitBtn = createStyledButton("🚀 Submit Exam", new Color(39, 174, 96));
        submitBtn.addActionListener(e -> submitExam());

        JButton reviewBtn = createStyledButton("📋 Review Answers", new Color(52, 152, 219));
        reviewBtn.addActionListener(e -> reviewAnswers());

        panel.add(reviewBtn);
        panel.add(submitBtn);

        return panel;
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

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void startTimer() {
        examTimer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimerDisplay();

            if (timeRemaining <= 0) {
                examTimer.stop();
                timeUpAutoSubmit();
            } else if (timeRemaining <= 300) {
                timerLabel.setForeground(Color.RED);
            }
        });
        examTimer.start();
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void timeUpAutoSubmit() {
        JOptionPane.showMessageDialog(this,
                "Time's up! Your exam will be submitted automatically.",
                "Time Up", JOptionPane.WARNING_MESSAGE);
        submitExam();
    }

    private void reviewAnswers() {
        int answered = studentAnswers.size();
        int total = questions.size();

        String message = String.format("""
            <html>
            <body style='font-family: Segoe UI; font-size: 14px;'>
            <h3>Answer Review</h3>
            <p>Questions Answered: <b>%d/%d</b></p>
            <p>Questions Remaining: <b>%d</b></p>
            <p>Time Remaining: <b>%02d:%02d</b></p>
            <br>
            <p style='color: %s;'>%s</p>
            </body>
            </html>
            """,
                answered, total, total - answered,
                timeRemaining / 60, timeRemaining % 60,
                answered == total ? "#27ae60" : "#e74c3c",
                answered == total ? "✅ All questions answered!" : "⚠️ You have unanswered questions!");

        JOptionPane.showMessageDialog(this, message, "Answer Review", JOptionPane.INFORMATION_MESSAGE);
    }

    private void submitExam() {
        int answered = studentAnswers.size();
        int total = questions.size();

        if (answered < total) {
            int result = JOptionPane.showConfirmDialog(this,
                    "You have answered " + answered + " out of " + total + " questions.\n" +
                            "Are you sure you want to submit?",
                    "Confirm Submission", JOptionPane.YES_NO_OPTION);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Calculate score
        double score = calculateScore();
        int totalPossibleMarks = questions.stream().mapToInt(Question::getMarks).sum();
        double percentage = totalPossibleMarks > 0 ? (score / totalPossibleMarks) * 100 : 0;

        // Stop timer
        if (examTimer != null) {
            examTimer.stop();
        }

        // Persist result
        Result result = new Result(student.getUserId(), exam.getExamId(), score, exam.getCreatedBy());
        try {
            ResultService.addResult(result);
        } catch (Exception ex) {
            System.err.println("Error saving exam result: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Show results
        String resultMessage = String.format("""
            <html>
            <body style='font-family: Segoe UI; font-size: 14px; text-align: center;'>
            <h2 style='color: #2c3e50;'>Exam Submitted Successfully! 🎉</h2>
            <div style='background: #f8f9fa; padding: 20px; border-radius: 10px; margin: 15px 0;'>
            <p style='font-size: 16px;'><b>Score: %.1f/%d</b></p>
            <p style='font-size: 18px; color: %s;'><b>Percentage: %.1f%%</b></p>
            <p>Questions Answered: %d/%d</p>
            </div>
            <p>Thank you for completing the exam!</p>
            </body>
            </html>
            """,
                score, totalPossibleMarks,
                percentage >= 50 ? "#27ae60" : "#e74c3c",
                percentage, answered, total);

        JOptionPane.showMessageDialog(this, resultMessage, "Exam Results", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private double calculateScore() {
        double score = 0;
        for (Question question : questions) {
            String studentAnswer = studentAnswers.get(question.getQuestionId());
            if (studentAnswer != null && question.getCorrectAnswer() != null) {
                if (studentAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                    score += question.getMarks();
                }
            }
        }
        return score;
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the exam?\n" +
                        "Your progress will be lost!",
                "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            if (examTimer != null) {
                examTimer.stop();
            }
            dispose();
        }
    }
}

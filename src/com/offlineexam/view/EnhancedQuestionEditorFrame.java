package com.offlineexam.view;

import com.offlineexam.model.Exam;
import com.offlineexam.model.User;
import com.offlineexam.model.Question;
import com.offlineexam.model.Option;
import com.offlineexam.service.ExamService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnhancedQuestionEditorFrame extends JFrame {
    private Exam exam;
    private User teacher;
    private List<Question> questions;
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;
    private JTextField[] optionFields; // Store option fields separately

    public EnhancedQuestionEditorFrame(Exam exam, User teacher) {
        this.exam = exam;
        this.teacher = teacher;
        this.questions = new ArrayList<>();
        this.questionListModel = new DefaultListModel<>();
        this.optionFields = new JTextField[4];

        setTitle("Add Questions - " + exam.getExamName());
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        // Main panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left panel - Question form
        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(new JScrollPane(formPanel));

        // Right panel - Question list
        JPanel listPanel = createListPanel();
        splitPane.setRightComponent(listPanel);

        add(splitPane);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel headerLabel = new JLabel("Add Question to: " + exam.getExamName());
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(44, 62, 80));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);

        // Form content
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setBorder(BorderFactory.createTitledBorder("Question Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Question Type
        gbc.gridx = 0; gbc.gridy = 0;
        formContent.add(new JLabel("Question Type:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"MCQ", "TRUE_FALSE", "ESSAY"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formContent.add(typeCombo, gbc);

        // Question Text
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        formContent.add(new JLabel("Question Text:"), gbc);

        gbc.gridy = 2;
        JTextArea questionTextArea = new JTextArea(3, 30);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        questionTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formContent.add(new JScrollPane(questionTextArea), gbc);

        // Marks
        gbc.gridy = 3; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formContent.add(new JLabel("Marks:"), gbc);

        gbc.gridx = 1;
        JSpinner marksSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1)); // Changed to integer
        marksSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formContent.add(marksSpinner, gbc);

        // Options Panel (for MCQ)
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel optionsPanel = createOptionsPanel();
        formContent.add(optionsPanel, gbc);

        // Correct Answer Panel
        gbc.gridy = 5;
        JPanel answerPanel = createAnswerPanel(typeCombo);
        formContent.add(answerPanel, gbc);

        // Add Question Button
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = createStyledButton("➕ Add Question", new Color(39, 174, 96));
        addButton.addActionListener(e -> addQuestion(questionTextArea, typeCombo, marksSpinner, answerPanel));
        formContent.add(addButton, gbc);

        panel.add(formContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Options (for MCQ)"));

        char[] optionLetters = {'A', 'B', 'C', 'D'};

        for (int i = 0; i < 4; i++) {
            panel.add(new JLabel("Option " + optionLetters[i] + ":"));
            optionFields[i] = new JTextField();
            optionFields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(optionFields[i]);
        }

        return panel;
    }

    private JPanel createAnswerPanel(JComboBox<String> typeCombo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Correct Answer"));

        // MCQ Answer
        JPanel mcqPanel = new JPanel(new FlowLayout());
        mcqPanel.add(new JLabel("Correct Option:"));
        JComboBox<String> correctOptionCombo = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        mcqPanel.add(correctOptionCombo);

        // True/False Answer
        JPanel tfPanel = new JPanel(new FlowLayout());
        ButtonGroup tfGroup = new ButtonGroup();
        JRadioButton trueBtn = new JRadioButton("True");
        JRadioButton falseBtn = new JRadioButton("False");
        tfGroup.add(trueBtn);
        tfGroup.add(falseBtn);
        tfPanel.add(trueBtn);
        tfPanel.add(falseBtn);
        trueBtn.setSelected(true);

        // Essay (No specific correct answer needed)
        JPanel essayPanel = new JPanel(new BorderLayout());
        essayPanel.add(new JLabel("Essay question - manual grading required"), BorderLayout.CENTER);

        // Card layout to switch between answer types
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.add(mcqPanel, "MCQ");
        cardPanel.add(tfPanel, "TRUE_FALSE");
        cardPanel.add(essayPanel, "ESSAY");

        // Store references
        panel.putClientProperty("cardPanel", cardPanel);
        panel.putClientProperty("correctOptionCombo", correctOptionCombo);
        panel.putClientProperty("trueBtn", trueBtn);

        // Switch panels based on question type
        typeCombo.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, (String) typeCombo.getSelectedItem());
        });

        panel.add(cardPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Added Questions"));

        questionList = new JList<>(questionListModel);
        questionList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(questionList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // List buttons
        JPanel listButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton removeBtn = createStyledButton("Remove Selected", new Color(231, 76, 60));
        JButton saveBtn = createStyledButton("💾 Save All Questions", new Color(52, 152, 219));

        removeBtn.addActionListener(e -> removeSelectedQuestion());
        saveBtn.addActionListener(e -> saveAllQuestions());

        listButtonPanel.add(removeBtn);
        listButtonPanel.add(saveBtn);
        panel.add(listButtonPanel, BorderLayout.SOUTH);

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

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    @SuppressWarnings("unchecked")
    private void addQuestion(JTextArea questionTextArea, JComboBox<String> typeCombo, JSpinner marksSpinner, JPanel answerPanel) {
        try {
            String questionText = questionTextArea.getText().trim();
            if (questionText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter question text.");
                return;
            }

            String questionType = (String) typeCombo.getSelectedItem();
            int marks = (Integer) marksSpinner.getValue(); // Now integer

            // Create question based on type
            Question question;

            if ("MCQ".equals(questionType)) {
                // Validate MCQ options
                if (optionFields[0].getText().trim().isEmpty() ||
                        optionFields[1].getText().trim().isEmpty() ||
                        optionFields[2].getText().trim().isEmpty() ||
                        optionFields[3].getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all options for MCQ question.");
                    return;
                }

                // Get correct answer
                JComboBox<String> correctOptionCombo = (JComboBox<String>) answerPanel.getClientProperty("correctOptionCombo");
                String correctAnswer = (String) correctOptionCombo.getSelectedItem();

                // Create MCQ question using the constructor that matches our model
                question = new Question(
                        exam.getExamId(),
                        questionText,
                        optionFields[0].getText().trim(),
                        optionFields[1].getText().trim(),
                        optionFields[2].getText().trim(),
                        optionFields[3].getText().trim(),
                        correctAnswer,
                        marks
                );

            } else if ("TRUE_FALSE".equals(questionType)) {
                // Get correct answer for True/False
                JRadioButton trueBtn = (JRadioButton) answerPanel.getClientProperty("trueBtn");
                String correctAnswer = trueBtn.isSelected() ? "True" : "False";

                // Create True/False question
                question = new Question(
                        exam.getExamId(),
                        questionText,
                        "True",  // Option A
                        "False", // Option B
                        "",      // Empty for C
                        "",      // Empty for D
                        correctAnswer,
                        marks
                );
                question.setQuestionType("TRUE_FALSE");

            } else { // ESSAY
                // Create Essay question
                question = new Question(
                        exam.getExamId(),
                        questionText,
                        "", "", "", "", "", marks
                );
                question.setQuestionType("ESSAY");
            }

            // Add to service and list
            if (ExamService.addQuestion(question)) {
                questions.add(question);
                questionListModel.addElement(questionText + " (" + questionType + ") - " + marks + " marks");

                JOptionPane.showMessageDialog(this, "Question added successfully!");

                // Clear form
                questionTextArea.setText("");
                marksSpinner.setValue(5);

                // Clear option fields for MCQ
                if ("MCQ".equals(questionType)) {
                    for (JTextField field : optionFields) {
                        if (field != null) field.setText("");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add question to database.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding question: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void removeSelectedQuestion() {
        int selectedIndex = questionList.getSelectedIndex();
        if (selectedIndex != -1) {
            Question question = questions.get(selectedIndex);
            // Note: Your current ExamService doesn't have deleteQuestion method
            // You would need to add it to ExamService
            questions.remove(selectedIndex);
            questionListModel.remove(selectedIndex);
            JOptionPane.showMessageDialog(this, "Question removed from list.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a question to remove.");
        }
    }

    private void saveAllQuestions() {
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions to save.");
            return;
        }

        int totalMarks = questions.stream().mapToInt(Question::getMarks).sum();

        JOptionPane.showMessageDialog(this,
                "All questions saved successfully!\n" +
                        "Total Questions: " + questions.size() + "\n" +
                        "Total Marks: " + totalMarks,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
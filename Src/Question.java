package com.offlineexam.model;

import java.util.List;
import java.util.ArrayList;

public class Question {
    private int questionId;
    private int examId;
    private String questionText;
    private String questionType;
    private int marks;
    private List<Option> options;
    // Flattened options used by DB/UI
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;

    // Basic constructor (legacy)
    public Question(int questionId, int examId, String questionText, String questionType, int marks) {
        this.questionId = questionId;
        this.examId = examId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.marks = marks;
        this.options = new ArrayList<>();
    }

    // Constructor for new questions (legacy)
    public Question(int examId, String questionText, String questionType, int marks) {
        this.examId = examId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.marks = marks;
        this.options = new ArrayList<>();
    }

    // Full constructor used by DB layer
    public Question(int questionId, int examId, String questionText,
                    String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer, int marks) {
        this.questionId = questionId;
        this.examId = examId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.marks = marks;
        this.questionType = "MCQ"; // default for options-based questions
        this.options = new ArrayList<>();
    }

    // Constructor used by UI when creating new questions with options
    public Question(int examId, String questionText,
                    String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer, int marks) {
        this.examId = examId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.marks = marks;
        this.questionType = "MCQ";
        this.options = new ArrayList<>();
    }

    // Getters and setters
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }

    public void addOption(Option option) {
        this.options.add(option);
    }

    // Helper method to get correct answer
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
}

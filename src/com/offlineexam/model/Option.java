package com.offlineexam.model;

public class Option {
    private int optionId;
    private int questionId;
    private String optionText;
    private String isCorrect;

    public Option(int optionId, int questionId, String optionText, String isCorrect) {
        this.optionId = optionId;
        this.questionId = questionId;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    // Constructor for new options
    public Option(int questionId, String optionText, String isCorrect) {
        this.questionId = questionId;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    // Getters and setters
    public int getOptionId() { return optionId; }
    public void setOptionId(int optionId) { this.optionId = optionId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public String getIsCorrect() { return isCorrect; }
    public void setIsCorrect(String isCorrect) { this.isCorrect = isCorrect; }

    public boolean isCorrect() {
        return "Y".equals(isCorrect);
    }
}
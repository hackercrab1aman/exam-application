package com.examapp.model;

public class Question {
    private int id;
    private int examId;
    private String text;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption; // 1, 2, 3, or 4
    
    public Question() {
        // Default constructor
        this.id = 0;
        this.examId = 0;
        this.text = "";
        this.option1 = "";
        this.option2 = "";
        this.option3 = "";
        this.option4 = "";
        this.correctOption = 1;
    }
    
    public Question(int id, int examId, String text, String option1, String option2, 
                   String option3, String option4, int correctOption) {
        this.id = id;
        this.examId = examId;
        this.text = text;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctOption = correctOption;
    }
    
    // Getters and setters remain the same as before
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getExamId() {
        return examId;
    }
    
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getOption1() {
        return option1;
    }
    
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    
    public String getOption2() {
        return option2;
    }
    
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    
    public String getOption3() {
        return option3;
    }
    
    public void setOption3(String option3) {
        this.option3 = option3;
    }
    
    public String getOption4() {
        return option4;
    }
    
    public void setOption4(String option4) {
        this.option4 = option4;
    }
    
    public int getCorrectOption() {
        return correctOption;
    }
    
    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
    
    public String getCorrectAnswerText() {
        switch (correctOption) {
            case 1: return option1;
            case 2: return option2;
            case 3: return option3;
            case 4: return option4;
            default: return "";
        }
    }
}
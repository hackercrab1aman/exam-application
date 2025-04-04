package com.examapp.model;

import java.util.Date;

public class ExamResult {
    private int id;
    private int studentId;
    private int examId;
    private String examTitle;
    private int score;
    private int totalQuestions;
    private Date completionDate;
    private String studentName; // Optional, used for displaying results
    
    public ExamResult() {
    }
    
    public ExamResult(int id, int studentId, int examId, String examTitle, 
                      int score, int totalQuestions, Date completionDate) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.examTitle = examTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.completionDate = completionDate;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getExamId() {
        return examId;
    }
    
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    public String getExamTitle() {
        return examTitle;
    }
    
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public Date getCompletionDate() {
        return completionDate;
    }
    
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    // Calculate the percentage score
    public double getPercentageScore() {
        if (totalQuestions == 0) return 0;
        return (score * 100.0) / totalQuestions;
    }
}
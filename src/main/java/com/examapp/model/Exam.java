package com.examapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Exam {
    private int id;
    private String title;
    private String description;
    private int durationMinutes;
    private int createdByTeacherId;
    private String createdByTeacherName;
    private Date createdAt;
    private List<Question> questions;
    
    public Exam() {
        this.questions = new ArrayList<>();
        this.durationMinutes = 30; // Default duration
    }
    
    public Exam(int id, String title, String description, int durationMinutes, 
                int createdByTeacherId, String createdByTeacherName, Date createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.createdByTeacherId = createdByTeacherId;
        this.createdByTeacherName = createdByTeacherName;
        this.createdAt = createdAt;
        this.questions = new ArrayList<>();
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public int getCreatedByTeacherId() {
        return createdByTeacherId;
    }
    
    public void setCreatedByTeacherId(int createdByTeacherId) {
        this.createdByTeacherId = createdByTeacherId;
    }
    
    public String getCreatedByTeacherName() {
        return createdByTeacherName;
    }
    
    public void setCreatedByTeacherName(String createdByTeacherName) {
        this.createdByTeacherName = createdByTeacherName;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<Question> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public void addQuestion(Question question) {
        this.questions.add(question);
    }
    
    public int getQuestionCount() {
        return this.questions.size();
    }
}
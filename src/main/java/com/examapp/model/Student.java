package com.examapp.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<ExamResult> examResults;

    public Student(int id, String username, String password, String name, String email) {
        super(id, username, password, name, email, "student");
        this.examResults = new ArrayList<>();
    }

    public List<ExamResult> getExamResults() {
        return examResults;
    }

    public void addExamResult(ExamResult result) {
        this.examResults.add(result);
    }
}
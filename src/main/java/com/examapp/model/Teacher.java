package com.examapp.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private List<Exam> createdExams;

    public Teacher(int id, String username, String password, String name, String email) {
        super(id, username, password, name, email, "teacher");
        this.createdExams = new ArrayList<>();
    }

    public List<Exam> getCreatedExams() {
        return createdExams;
    }

    public void addCreatedExam(Exam exam) {
        this.createdExams.add(exam);
    }
}
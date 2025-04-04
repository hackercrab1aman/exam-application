package com.examapp.dao;

import com.examapp.model.Exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamDAO {
    
    // Create a new exam
    public int createExam(Exam exam) throws SQLException {
        String sql = "INSERT INTO exams (title, description, duration_minutes, created_by_teacher_id, created_by_teacher_name) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, exam.getTitle());
            stmt.setString(2, exam.getDescription());
            stmt.setInt(3, exam.getDurationMinutes());
            stmt.setInt(4, exam.getCreatedByTeacherId());
            stmt.setString(5, exam.getCreatedByTeacherName());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating exam failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int examId = generatedKeys.getInt(1);
                    exam.setId(examId);
                    return examId;
                } else {
                    throw new SQLException("Creating exam failed, no ID obtained.");
                }
            }
        }
    }
    
    // Get all exams
    public List<Exam> getAllExams() throws SQLException {
        String sql = "SELECT * FROM exams ORDER BY created_at DESC";
        List<Exam> exams = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                exams.add(extractExamFromResultSet(rs));
            }
        }
        
        return exams;
    }
    
    // Get exams created by a specific teacher
    public List<Exam> getExamsByTeacher(int teacherId) throws SQLException {
        String sql = "SELECT * FROM exams WHERE created_by_teacher_id = ? ORDER BY created_at DESC";
        List<Exam> exams = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, teacherId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exams.add(extractExamFromResultSet(rs));
                }
            }
        }
        
        return exams;
    }
    
    // Get a specific exam by ID
    public Exam getExamById(int examId) throws SQLException {
        String sql = "SELECT * FROM exams WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractExamFromResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    // Update an existing exam
    public void updateExam(Exam exam) throws SQLException {
        String sql = "UPDATE exams SET title = ?, description = ?, duration_minutes = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, exam.getTitle());
            stmt.setString(2, exam.getDescription());
            stmt.setInt(3, exam.getDurationMinutes());
            stmt.setInt(4, exam.getId());
            
            stmt.executeUpdate();
        }
    }
    
    // Delete an exam
    public void deleteExam(int examId) throws SQLException {
        String sql = "DELETE FROM exams WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            stmt.executeUpdate();
        }
    }
    
    // Helper method to extract exam from result set
    private Exam extractExamFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        int durationMinutes = rs.getInt("duration_minutes");
        int createdByTeacherId = rs.getInt("created_by_teacher_id");
        String createdByTeacherName = rs.getString("created_by_teacher_name");
        Date createdAt = rs.getTimestamp("created_at");
        
        return new Exam(id, title, description, durationMinutes, 
                        createdByTeacherId, createdByTeacherName, createdAt);
    }
}
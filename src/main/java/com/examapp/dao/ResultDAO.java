package com.examapp.dao;

import com.examapp.model.ExamResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultDAO {
    
    // Create a new exam result
    public int createResult(ExamResult result) throws SQLException {
        String sql = "INSERT INTO results (student_id, exam_id, exam_title, score, total_questions) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, result.getStudentId());
            stmt.setInt(2, result.getExamId());
            stmt.setString(3, result.getExamTitle());
            stmt.setInt(4, result.getScore());
            stmt.setInt(5, result.getTotalQuestions());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating result failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int resultId = generatedKeys.getInt(1);
                    result.setId(resultId);
                    return resultId;
                } else {
                    throw new SQLException("Creating result failed, no ID obtained.");
                }
            }
        }
    }
    
    // Get results for a specific exam
    public List<ExamResult> getResultsForExam(int examId) throws SQLException {
        String sql = "SELECT r.*, u.name as student_name " +
                     "FROM results r " +
                     "JOIN users u ON r.student_id = u.id " +
                     "WHERE r.exam_id = ? " +
                     "ORDER BY r.completion_date DESC";
        
        List<ExamResult> results = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(extractResultFromResultSet(rs));
                }
            }
        }
        
        return results;
    }
    
    // Get results for a specific student
    public List<ExamResult> getResultsForStudent(int studentId) throws SQLException {
        String sql = "SELECT * FROM results WHERE student_id = ? ORDER BY completion_date DESC";
        List<ExamResult> results = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(extractResultFromResultSet(rs));
                }
            }
        }
        
        return results;
    }
    
    // Get a specific result by ID
    public ExamResult getResultById(int resultId) throws SQLException {
        String sql = "SELECT r.*, u.name as student_name " +
                     "FROM results r " +
                     "JOIN users u ON r.student_id = u.id " +
                     "WHERE r.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resultId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractResultFromResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    // Check if a student has already taken an exam
    public boolean hasStudentTakenExam(int studentId, int examId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM results WHERE student_id = ? AND exam_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    // Get the number of students who have taken an exam
    public int getResultCountForExam(int examId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM results WHERE exam_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    // Delete a result
    public void deleteResult(int resultId) throws SQLException {
        String sql = "DELETE FROM results WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resultId);
            stmt.executeUpdate();
        }
    }
    
    // Delete all results for an exam
    public void deleteResultsForExam(int examId) throws SQLException {
        String sql = "DELETE FROM results WHERE exam_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            stmt.executeUpdate();
        }
    }
    
    // Helper method to extract result from result set
    private ExamResult extractResultFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int studentId = rs.getInt("student_id");
        int examId = rs.getInt("exam_id");
        String examTitle = rs.getString("exam_title");
        int score = rs.getInt("score");
        int totalQuestions = rs.getInt("total_questions");
        Date completionDate = rs.getTimestamp("completion_date");
        
        ExamResult result = new ExamResult(id, studentId, examId, examTitle, score, totalQuestions, completionDate);
        
        // Add student name if available in result set
        try {
            String studentName = rs.getString("student_name");
            if (studentName != null) {
                result.setStudentName(studentName);
            }
        } catch (SQLException e) {
            // Student name not included in the query, that's ok
        }
        
        return result;
    }
}
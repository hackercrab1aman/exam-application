package com.examapp.dao;

import com.examapp.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    
    // Create a new question
    public int createQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO questions (exam_id, text, option1, option2, option3, option4, correct_option) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        System.out.println("Creating question for exam ID: " + question.getExamId());
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, question.getExamId());
            stmt.setString(2, question.getText());
            stmt.setString(3, question.getOption1());
            stmt.setString(4, question.getOption2());
            stmt.setString(5, question.getOption3());
            stmt.setString(6, question.getOption4());
            stmt.setInt(7, question.getCorrectOption());
            
            System.out.println("Executing insert: " + stmt.toString());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating question failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int questionId = generatedKeys.getInt(1);
                    question.setId(questionId);
                    System.out.println("Question created with ID: " + questionId);
                    return questionId;
                } else {
                    throw new SQLException("Creating question failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating question: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Get all questions for a specific exam
    public List<Question> getQuestionsForExam(int examId) throws SQLException {
        String sql = "SELECT * FROM questions WHERE exam_id = ?";
        List<Question> questions = new ArrayList<>();
        
        System.out.println("Getting questions for exam ID: " + examId);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            
            System.out.println("Executing query: " + stmt.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = extractQuestionFromResultSet(rs);
                    questions.add(question);
                    System.out.println("Found question ID: " + question.getId() + " - " + question.getText());
                }
            }
            
            System.out.println("Found " + questions.size() + " questions for exam ID " + examId);
        } catch (SQLException e) {
            System.err.println("Error getting questions: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        return questions;
    }
    
    // Get the number of questions for an exam
    public int getQuestionCountForExam(int examId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM questions WHERE exam_id = ?";
        
        System.out.println("Getting question count for exam ID: " + examId);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Found " + count + " questions for exam ID " + examId);
                    return count;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting question count: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        return 0;
    }
    
    // Get a specific question by ID
    public Question getQuestionById(int questionId) throws SQLException {
        String sql = "SELECT * FROM questions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, questionId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractQuestionFromResultSet(rs);
                }
            }
        }
        
        return null;
    }
    
    // Update an existing question
    public void updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE questions SET text = ?, option1 = ?, option2 = ?, " +
                     "option3 = ?, option4 = ?, correct_option = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, question.getText());
            stmt.setString(2, question.getOption1());
            stmt.setString(3, question.getOption2());
            stmt.setString(4, question.getOption3());
            stmt.setString(5, question.getOption4());
            stmt.setInt(6, question.getCorrectOption());
            stmt.setInt(7, question.getId());
            
            stmt.executeUpdate();
        }
    }
    
    // Delete a question
    public void deleteQuestion(int questionId) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, questionId);
            stmt.executeUpdate();
        }
    }
    
    // Delete all questions for an exam
    public void deleteQuestionsForExam(int examId) throws SQLException {
        String sql = "DELETE FROM questions WHERE exam_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, examId);
            stmt.executeUpdate();
        }
    }
    
    // Helper method to extract question from result set
    private Question extractQuestionFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int examId = rs.getInt("exam_id");
        String text = rs.getString("text");
        String option1 = rs.getString("option1");
        String option2 = rs.getString("option2");
        String option3 = rs.getString("option3");
        String option4 = rs.getString("option4");
        int correctOption = rs.getInt("correct_option");
        
        return new Question(id, examId, text, option1, option2, option3, option4, correctOption);
    }
}
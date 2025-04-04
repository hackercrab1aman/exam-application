package com.examapp.dao;

import com.examapp.model.User;
import com.examapp.model.Student;
import com.examapp.model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    // Get user by username
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        
        System.out.println("Executing query for username: " + username); // Debug print
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            System.out.println("Executing query: " + stmt.toString()); // Debug print
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User found in database"); // Debug print
                    
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String userType = rs.getString("user_type");
                    
                    System.out.println("User details: ID=" + id + ", Name=" + name + ", Type=" + userType); // Debug print
                    
                    if (userType.equals("teacher")) {
                        user = new Teacher(id, username, password, name, email);
                    } else {
                        user = new Student(id, username, password, name, email);
                    }
                } else {
                    System.out.println("No user found with username: " + username); // Debug print
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getUserByUsername: " + e.getMessage()); // Debug print
            e.printStackTrace();
            throw e;
        }
        
        return user;
    }
    
    // Create new user
    public User createUser(String username, String password, String name, String email, String userType) throws SQLException {
        String sql = "INSERT INTO users (username, password, name, email, user_type) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); // Assume password is already hashed
            stmt.setString(3, name);
            stmt.setString(4, email);
            stmt.setString(5, userType);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    
                    if (userType.equals("teacher")) {
                        return new Teacher(id, username, password, name, email);
                    } else {
                        return new Student(id, username, password, name, email);
                    }
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }
}
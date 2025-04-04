package com.examapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/exam_app";
    private static final String USER = "root";
    private static final String PASSWORD = "Aman@123."; // Update with your MySQL password if needed
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create and return connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Failed to connect to database: " + e.getMessage());
        }
    }
    
    // Test connection method for debugging
    @SuppressWarnings("CallToPrintStackTrace")
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
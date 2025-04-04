package com.examapp;

import com.examapp.dao.DatabaseConnection;
import com.examapp.ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Main class of the Exam Application
 * Serves as the entry point for the application
 */
public class Main {
    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test database connection
        try {
            DatabaseConnection.getConnection();
            System.out.println("Database connection successful");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Launch the login screen
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
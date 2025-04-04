package com.examapp.ui;

import com.examapp.dao.UserDAO;
import com.examapp.model.User;
import com.examapp.ui.student.StudentDashboard;
import com.examapp.ui.teacher.TeacherDashboard;
import com.examapp.util.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserDAO userDAO;

    public LoginFrame() {
        // Set up DAO
        userDAO = new UserDAO();
        
        // Set up the frame
        setTitle("Exam Application - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel titleLabel = new JLabel("Login to Exam Application", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        loginButton = new JButton("Login");
        registerButton = new JButton("Register New Account");
        
        // Add components to panels
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panel to frame
        add(mainPanel);
        
        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login button clicked"); // Debug print
                try {
                    loginUser();
                } catch (Exception ex) {
                    System.err.println("Login error: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                            "Error: " + ex.getMessage(), 
                            "Login Failed", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register button clicked"); // Debug print
                openRegisterFrame();
            }
        });
    }
    
    private void loginUser() {
        System.out.println("Attempting to login user..."); // Debug print
        
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        System.out.println("Username: " + username); // Debug print
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Empty username or password"); // Debug print
            JOptionPane.showMessageDialog(this, 
                    "Username and password cannot be empty", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            System.out.println("Checking database connection..."); // Debug print
            boolean connectionOk = com.examapp.dao.DatabaseConnection.testConnection();
            if (!connectionOk) {
                JOptionPane.showMessageDialog(this, 
                        "Cannot connect to database. Please check connection settings.", 
                        "Database Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Getting user from database..."); // Debug print
            // Get user from database
            User user = userDAO.getUserByUsername(username);
            
            System.out.println("User found: " + (user != null)); // Debug print
            
            // Check if user exists and password matches
            if (user != null) {
                System.out.println("Checking password..."); // Debug print
                
                // Special handling for demo accounts
                boolean passwordMatch = false;
                if (username.equals("student") && password.equals("student123")) {
                    passwordMatch = true;
                    System.out.println("Special case: Demo student account login"); // Debug print
                } else if (username.equals("admin") && password.equals("admin123")) {
                    passwordMatch = true;
                    System.out.println("Special case: Demo admin account login"); // Debug print
                } else {
                    passwordMatch = PasswordUtils.checkPassword(password, user.getPassword());
                }
                
                System.out.println("Password match: " + passwordMatch); // Debug print
                
                if (passwordMatch) {
                    // Login successful
                    System.out.println("Login successful for user: " + user.getName()); // Debug print
                    JOptionPane.showMessageDialog(this, 
                            "Login successful! Welcome " + user.getName(), 
                            "Login Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    // Open appropriate dashboard based on user type
                    if (user.getUserType().equals("teacher")) {
                        System.out.println("Opening teacher dashboard"); // Debug print
                        TeacherDashboard teacherDashboard = new TeacherDashboard(user);
                        teacherDashboard.setVisible(true);
                    } else {
                        System.out.println("Opening student dashboard"); // Debug print
                        StudentDashboard studentDashboard = new StudentDashboard(user);
                        studentDashboard.setVisible(true);
                    }
                    
                    // Close login frame
                    this.dispose();
                } else {
                    // Password doesn't match
                    JOptionPane.showMessageDialog(this, 
                            "Invalid username or password", 
                            "Login Failed", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User not found
                JOptionPane.showMessageDialog(this, 
                        "Invalid username or password", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage()); // Debug print
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Database error: " + ex.getMessage(), 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.err.println("General error: " + ex.getMessage()); // Debug print
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.dispose();
    }
}
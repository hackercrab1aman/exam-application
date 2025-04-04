package com.examapp.ui;

import com.examapp.dao.UserDAO;
import com.examapp.util.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField nameField;
    private JTextField emailField;
    private JRadioButton studentRadio;
    private JRadioButton teacherRadio;
    private JTextField teacherCodeField;
    private JButton registerButton;
    private JButton backButton;
    
    private UserDAO userDAO;
    
    private static final String TEACHER_REGISTRATION_CODE = "TEACHER123"; // Special code for teacher registration
    
    public RegisterFrame() {
        userDAO = new UserDAO();
        
        // Set up the frame
        setTitle("Exam Application - Register");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Register New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);
        
        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField(20);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        
        // User Type
        JLabel userTypeLabel = new JLabel("Register as:");
        studentRadio = new JRadioButton("Student");
        teacherRadio = new JRadioButton("Teacher");
        
        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(studentRadio);
        userTypeGroup.add(teacherRadio);
        
        // Teacher code
        JLabel teacherCodeLabel = new JLabel("Teacher Registration Code:");
        teacherCodeField = new JTextField(20);
        
        // Buttons
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(userTypeLabel, gbc);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(studentRadio);
        radioPanel.add(teacherRadio);
        
        gbc.gridx = 1;
        formPanel.add(radioPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(teacherCodeLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(teacherCodeField, gbc);
        
        // Add buttons to panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        // Add panels to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Set default selections
        studentRadio.setSelected(true);
        teacherCodeField.setEnabled(false);
        
        // Add action listeners
        studentRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherCodeField.setEnabled(false);
            }
        });
        
        teacherRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherCodeField.setEnabled(true);
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToLogin();
            }
        });
    }
    
    private void registerUser() {
        // Get field values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        boolean isTeacher = teacherRadio.isSelected();
        String teacherCode = teacherCodeField.getText().trim();
        
        // Validate fields
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please fill all required fields", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                    "Passwords do not match", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (isTeacher && !teacherCode.equals(TEACHER_REGISTRATION_CODE)) {
            JOptionPane.showMessageDialog(this, 
                    "Invalid teacher registration code", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Check if username already exists
            if (userDAO.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, 
                        "Username already exists. Please choose another.", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Hash password
            String hashedPassword = PasswordUtils.hashPassword(password);
            
            // Create user
            String userType = isTeacher ? "teacher" : "student";
            userDAO.createUser(username, hashedPassword, name, email, userType);
            
            JOptionPane.showMessageDialog(this, 
                    "Registration successful! You can now login.", 
                    "Registration Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Go back to login
            goBackToLogin();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error during registration: " + ex.getMessage(), 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void goBackToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
}
package com.examapp.ui.student;

import com.examapp.model.User;
import com.examapp.model.Student;
import com.examapp.dao.ExamDAO;
import com.examapp.model.Exam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentDashboard extends JFrame {
    private Student student;
    private ExamDAO examDAO;
    private JPanel mainPanel;
    private JPanel examListPanel;
    private JButton refreshButton;
    private JButton logoutButton;

    public StudentDashboard(User user) {
        if (!(user instanceof Student)) {
            throw new IllegalArgumentException("User must be a Student");
        }
        this.student = (Student) user;
        this.examDAO = new ExamDAO();
        
        // Set up the frame
        setTitle("Student Dashboard - " + student.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh Exams");
        logoutButton = new JButton("Logout");
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Create exam list panel
        examListPanel = new JPanel();
        examListPanel.setLayout(new BoxLayout(examListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(examListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Exams"));
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshExamList();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        // Load initial exam list
        refreshExamList();
    }
    
    private void refreshExamList() {
        examListPanel.removeAll();
        
        try {
            List<Exam> exams = examDAO.getAllExams();
            
            if (exams.isEmpty()) {
                JLabel noExamsLabel = new JLabel("No exams available at this time.");
                noExamsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                examListPanel.add(noExamsLabel);
            } else {
                for (Exam exam : exams) {
                    JPanel examPanel = createExamPanel(exam);
                    examListPanel.add(examPanel);
                    examListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            examListPanel.revalidate();
            examListPanel.repaint();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading exams: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private JPanel createExamPanel(final Exam exam) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Exam details
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JLabel titleLabel = new JLabel(exam.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel descLabel = new JLabel("Description: " + exam.getDescription());
        JLabel infoLabel = new JLabel(String.format("Created by: %s | Duration: %d minutes | Questions: %d",
                exam.getCreatedByTeacherName(), exam.getDurationMinutes(), exam.getQuestionCount()));
        
        detailsPanel.add(titleLabel);
        detailsPanel.add(descLabel);
        detailsPanel.add(infoLabel);
        
        // Button to take exam
        JButton takeExamButton = new JButton("Take Exam");
        takeExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                takeExam(exam);
            }
        });
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(takeExamButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void takeExam(Exam exam) {
        // Open exam panel
        ExamPanel examPanel = new ExamPanel(student, exam);
        examPanel.setVisible(true);
        this.dispose();
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
                
        if (result == JOptionPane.YES_OPTION) {
            com.examapp.ui.LoginFrame loginFrame = new com.examapp.ui.LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}
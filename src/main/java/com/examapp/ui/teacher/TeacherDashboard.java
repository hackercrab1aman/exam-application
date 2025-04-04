package com.examapp.ui.teacher;

import com.examapp.model.User;
import com.examapp.model.Teacher;
import com.examapp.dao.ExamDAO;
import com.examapp.model.Exam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TeacherDashboard extends JFrame {
    private Teacher teacher;
    private ExamDAO examDAO;
    private JTabbedPane tabbedPane;
    private CreateExamPanel createExamPanel;
    private ManageExamsPanel manageExamsPanel;
    private JButton logoutButton;

    public TeacherDashboard(User user) {
        if (!(user instanceof Teacher)) {
            throw new IllegalArgumentException("User must be a Teacher");
        }
        this.teacher = (Teacher) user;
        this.examDAO = new ExamDAO();
        
        // Set up the frame
        setTitle("Teacher Dashboard - " + teacher.getName());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + teacher.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        buttonPanel.add(logoutButton);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create panels for tabs
        createExamPanel = new CreateExamPanel(teacher);
        manageExamsPanel = new ManageExamsPanel(teacher);
        
        // Add tabs
        tabbedPane.addTab("Create Exam", createExamPanel);
        tabbedPane.addTab("Manage Exams", manageExamsPanel);
        
        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add action listeners
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        // Set up tab change listener to refresh data
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == manageExamsPanel) {
                manageExamsPanel.refreshExamsList();
            }
        });
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
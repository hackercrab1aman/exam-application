package com.examapp.ui.student;

import com.examapp.model.Exam;
import com.examapp.model.Student;
import com.examapp.model.Question;
import com.examapp.dao.QuestionDAO;
import com.examapp.dao.ResultDAO;
import com.examapp.model.ExamResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExamPanel extends JFrame {
    private Student student;
    private Exam exam;
    private List<Question> questions;
    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;
    
    private JPanel mainPanel;
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JRadioButton option1Radio, option2Radio, option3Radio, option4Radio;
    private ButtonGroup optionGroup;
    private JButton prevButton, nextButton, submitButton;
    private JLabel timerLabel;
    private JProgressBar progressBar;
    
    private int currentQuestionIndex = 0;
    private int[] selectedAnswers;
    private Timer examTimer;
    private int timeRemaining;
    
    public ExamPanel(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.questionDAO = new QuestionDAO();
        this.resultDAO = new ResultDAO();
        
        // Set up the frame
        setTitle("Exam: " + exam.getTitle());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            // Load questions for the exam
            this.questions = questionDAO.getQuestionsForExam(exam.getId());
            System.out.println("Loaded " + questions.size() + " questions for exam ID " + exam.getId());
            
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No questions found for this exam.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                backToDashboard();
                return;
            }
            
            // Initialize selected answers array
            selectedAnswers = new int[questions.size()];
            for (int i = 0; i < selectedAnswers.length; i++) {
                selectedAnswers[i] = 0; // 0 means no answer selected yet
            }
            
            // Initialize the UI
            initializeUI();
            
            // Start the exam timer
            startTimer();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading exam questions: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            backToDashboard();
        }
    }
    
    private void initializeUI() {
        // Add window closing listener to prevent accidental closing during exam
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(ExamPanel.this, 
                    "Are you sure you want to quit the exam? Your progress will be lost.", 
                    "Quit Exam?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    if (examTimer != null) {
                        examTimer.cancel();
                    }
                    backToDashboard();
                }
            }
        });
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header panel with timer and progress info
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Timer label
        timerLabel = new JLabel("Time Remaining: " + formatTime(timeRemaining), JLabel.LEFT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Progress bar
        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(currentQuestionIndex + 1);
        progressBar.setStringPainted(true);
        progressBar.setString("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        
        headerPanel.add(timerLabel, BorderLayout.WEST);
        headerPanel.add(progressBar, BorderLayout.EAST);
        
        // Create question panel
        questionPanel = new JPanel(new BorderLayout(10, 10));
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        option1Radio = new JRadioButton();
        option2Radio = new JRadioButton();
        option3Radio = new JRadioButton();
        option4Radio = new JRadioButton();
        
        optionGroup = new ButtonGroup();
        optionGroup.add(option1Radio);
        optionGroup.add(option2Radio);
        optionGroup.add(option3Radio);
        optionGroup.add(option4Radio);
        
        optionsPanel.add(option1Radio);
        optionsPanel.add(option2Radio);
        optionsPanel.add(option3Radio);
        optionsPanel.add(option4Radio);
        
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(optionsPanel, BorderLayout.CENTER);
        
        // Create navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit Exam");
        
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(submitButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer();
                showPreviousQuestion();
            }
        });
        
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer();
                showNextQuestion();
            }
        });
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitExam();
            }
        });
        
        // Show the first question
        showQuestion(0);
    }
    
    private void showQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            return;
        }
        
        currentQuestionIndex = index;
        Question question = questions.get(index);
        
        // Update question text and options
        questionLabel.setText("<html><div style='width:600px'>" + (index + 1) + ". " + question.getText() + "</div></html>");
        option1Radio.setText("<html><div style='width:500px'>A. " + question.getOption1() + "</div></html>");
        option2Radio.setText("<html><div style='width:500px'>B. " + question.getOption2() + "</div></html>");
        option3Radio.setText("<html><div style='width:500px'>C. " + question.getOption3() + "</div></html>");
        option4Radio.setText("<html><div style='width:500px'>D. " + question.getOption4() + "</div></html>");
        
        // Clear selection
        optionGroup.clearSelection();
        
        // Set selected answer if already answered
        if (selectedAnswers[index] > 0) {
            switch (selectedAnswers[index]) {
                case 1:
                    option1Radio.setSelected(true);
                    break;
                case 2:
                    option2Radio.setSelected(true);
                    break;
                case 3:
                    option3Radio.setSelected(true);
                    break;
                case 4:
                    option4Radio.setSelected(true);
                    break;
            }
        }
        
        // Update buttons and progress
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < questions.size() - 1);
        
        progressBar.setValue(index + 1);
        progressBar.setString("Question " + (index + 1) + " of " + questions.size());
    }
    
    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            showQuestion(currentQuestionIndex + 1);
        }
    }
    
    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            showQuestion(currentQuestionIndex - 1);
        }
    }
    
    private void saveCurrentAnswer() {
        if (option1Radio.isSelected()) {
            selectedAnswers[currentQuestionIndex] = 1;
        } else if (option2Radio.isSelected()) {
            selectedAnswers[currentQuestionIndex] = 2;
        } else if (option3Radio.isSelected()) {
            selectedAnswers[currentQuestionIndex] = 3;
        } else if (option4Radio.isSelected()) {
            selectedAnswers[currentQuestionIndex] = 4;
        }
    }
    
    private void startTimer() {
        timeRemaining = exam.getDurationMinutes() * 60; // Convert minutes to seconds
        
        examTimer = new Timer();
        examTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                updateTimerLabel();
                
                if (timeRemaining <= 0) {
                    // Time's up
                    examTimer.cancel();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(ExamPanel.this,
                                    "Time's up! Your exam will be submitted automatically.",
                                    "Time's Up",
                                    JOptionPane.INFORMATION_MESSAGE);
                            submitExam();
                        }
                    });
                }
            }
        }, 1000, 1000); // Update every second
    }
    
    private void updateTimerLabel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                timerLabel.setText("Time Remaining: " + formatTime(timeRemaining));
                
                // Change color to red when less than 5 minutes remaining
                if (timeRemaining < 300) {
                    timerLabel.setForeground(Color.RED);
                }
            }
        });
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    
    private void submitExam() {
        // Ask for confirmation
        int confirmResult = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to submit your exam?",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION);
                
        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Stop the timer
        if (examTimer != null) {
            examTimer.cancel();
        }
        
        // Save the current answer
        saveCurrentAnswer();
        
        // Calculate score
        int score = 0;
        int answeredQuestions = 0;
        
        for (int i = 0; i < questions.size(); i++) {
            if (selectedAnswers[i] > 0) {
                answeredQuestions++;
                if (selectedAnswers[i] == questions.get(i).getCorrectOption()) {
                    score++;
                }
            }
        }
        
        // Create and save exam result
        try {
            ExamResult examResult = new ExamResult();
            examResult.setStudentId(student.getId());
            examResult.setExamId(exam.getId());
            examResult.setExamTitle(exam.getTitle());
            examResult.setScore(score);
            examResult.setTotalQuestions(questions.size());
            examResult.setCompletionDate(new Date());
            
            System.out.println("Saving exam result: Score=" + score + ", Total=" + questions.size());
            resultDAO.createResult(examResult);
            
            // Show result dialog
            JOptionPane.showMessageDialog(this,
                    "Exam submitted successfully!\n\n" +
                    "Your score: " + score + " out of " + questions.size() + "\n" +
                    "Percentage: " + String.format("%.1f%%", (score * 100.0 / questions.size())) + "\n" +
                    "Questions answered: " + answeredQuestions + " out of " + questions.size(),
                    "Exam Result",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Go back to dashboard
            backToDashboard();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving exam result: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void backToDashboard() {
        StudentDashboard dashboard = new StudentDashboard(student);
        dashboard.setVisible(true);
        this.dispose();
    }
}
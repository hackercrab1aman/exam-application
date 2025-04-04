package com.examapp.ui.teacher;

import com.examapp.dao.ExamDAO;
import com.examapp.dao.QuestionDAO;
import com.examapp.dao.ResultDAO;
import com.examapp.model.Exam;
import com.examapp.model.Question;
import com.examapp.model.ExamResult;
import com.examapp.model.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ManageExamsPanel extends JPanel {
    private Teacher teacher;
    private ExamDAO examDAO;
    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;
    
    private JPanel examsPanel;
    private JButton refreshButton;
    
    public ManageExamsPanel(Teacher teacher) {
        this.teacher = teacher;
        this.examDAO = new ExamDAO();
        this.questionDAO = new QuestionDAO();
        this.resultDAO = new ResultDAO();
        
        // Set up the panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header with refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Manage Your Exams");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        refreshButton = new JButton("Refresh");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);
        
        // Create scroll pane for exams
        examsPanel = new JPanel();
        examsPanel.setLayout(new BoxLayout(examsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(examsPanel);
        
        // Add components to panel
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add action listeners
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshExamsList();
            }
        });
        
        // Load exams initially
        refreshExamsList();
    }
    
    public void refreshExamsList() {
        examsPanel.removeAll();
        
        try {
            List<Exam> exams = examDAO.getExamsByTeacher(teacher.getId());
            
            if (exams.isEmpty()) {
                JLabel noExamsLabel = new JLabel("You haven't created any exams yet.");
                noExamsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                examsPanel.add(noExamsLabel);
            } else {
                for (Exam exam : exams) {
                    JPanel examPanel = createExamPanel(exam);
                    examsPanel.add(examPanel);
                    examsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            examsPanel.revalidate();
            examsPanel.repaint();
            
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
        
        try {
            int questionCount = questionDAO.getQuestionCountForExam(exam.getId());
            int resultCount = resultDAO.getResultCountForExam(exam.getId());
            
            JLabel questionsLabel = new JLabel(String.format("Questions: %d | Duration: %d minutes", 
                    questionCount, exam.getDurationMinutes()));
            
            JLabel resultsLabel = new JLabel(String.format("Completed by %d students", resultCount));
            
            detailsPanel.add(titleLabel);
            detailsPanel.add(questionsLabel);
            detailsPanel.add(resultsLabel);
            
        } catch (SQLException e) {
            detailsPanel.add(titleLabel);
            detailsPanel.add(new JLabel("Error loading exam details"));
            e.printStackTrace();
        }
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JButton viewQuestionsButton = new JButton("View Questions");
        JButton viewResultsButton = new JButton("View Results");
        JButton deleteExamButton = new JButton("Delete Exam");
        
        buttonsPanel.add(viewQuestionsButton);
        buttonsPanel.add(viewResultsButton);
        buttonsPanel.add(deleteExamButton);
        
        // Add action listeners
        viewQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewExamQuestions(exam);
            }
        });
        
        viewResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewExamResults(exam);
            }
        });
        
        deleteExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExam(exam);
            }
        });
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void viewExamQuestions(Exam exam) {
        try {
            List<Question> questions = questionDAO.getQuestionsForExam(exam.getId());
            
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No questions found for this exam.",
                        "No Questions",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create a dialog to display questions
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Questions for " + exam.getTitle(),
                    true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            
            JPanel questionListPanel = new JPanel();
            questionListPanel.setLayout(new BoxLayout(questionListPanel, BoxLayout.Y_AXIS));
            
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                JPanel qPanel = createQuestionViewPanel(i + 1, q);
                questionListPanel.add(qPanel);
                questionListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            
            JScrollPane scrollPane = new JScrollPane(questionListPanel);
            dialog.add(scrollPane);
            
            dialog.setVisible(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading questions: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private JPanel createQuestionViewPanel(int number, Question question) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Question " + number),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        panel.add(new JLabel("<html><b>Question:</b> " + question.getText() + "</html>"));
        
        // Highlight correct option in green
        panel.add(new JLabel("<html>1. " + 
                (question.getCorrectOption() == 1 ? "<font color='green'><b>" : "") + 
                question.getOption1() + 
                (question.getCorrectOption() == 1 ? "</b></font>" : "") + 
                "</html>"));
        
        panel.add(new JLabel("<html>2. " + 
                (question.getCorrectOption() == 2 ? "<font color='green'><b>" : "") + 
                question.getOption2() + 
                (question.getCorrectOption() == 2 ? "</b></font>" : "") + 
                "</html>"));
        
        panel.add(new JLabel("<html>3. " + 
                (question.getCorrectOption() == 3 ? "<font color='green'><b>" : "") + 
                question.getOption3() + 
                (question.getCorrectOption() == 3 ? "</b></font>" : "") + 
                "</html>"));
        
        panel.add(new JLabel("<html>4. " + 
                (question.getCorrectOption() == 4 ? "<font color='green'><b>" : "") + 
                question.getOption4() + 
                (question.getCorrectOption() == 4 ? "</b></font>" : "") + 
                "</html>"));
        
        return panel;
    }
    
    private void viewExamResults(Exam exam) {
        try {
            List<ExamResult> results = resultDAO.getResultsForExam(exam.getId());
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No students have taken this exam yet.",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create a dialog to display results
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Results for " + exam.getTitle(),
                    true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            
            // Create table model
            String[] columnNames = {"Student", "Score", "Percentage", "Date"};
            Object[][] data = new Object[results.size()][4];
            
            for (int i = 0; i < results.size(); i++) {
                ExamResult result = results.get(i);
                data[i][0] = result.getStudentName();
                data[i][1] = result.getScore() + "/" + result.getTotalQuestions();
                data[i][2] = String.format("%.1f%%", result.getScore() * 100.0 / result.getTotalQuestions());
                data[i][3] = result.getCompletionDate();
            }
            
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            dialog.add(scrollPane);
            
            dialog.setVisible(true);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading results: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteExam(Exam exam) {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this exam?\nAll questions and student results will also be deleted.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (result == JOptionPane.YES_OPTION) {
            try {
                examDAO.deleteExam(exam.getId());
                JOptionPane.showMessageDialog(this,
                        "Exam deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshExamsList();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting exam: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
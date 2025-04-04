package com.examapp.ui.teacher;

import com.examapp.dao.ExamDAO;
import com.examapp.dao.QuestionDAO;
import com.examapp.model.Exam;
import com.examapp.model.Question;
import com.examapp.model.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateExamPanel extends JPanel {
    private Teacher teacher;
    private ExamDAO examDAO;
    private QuestionDAO questionDAO;
    
    // Exam details components
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner durationSpinner;
    
    // Question components
    private List<QuestionPanel> questionPanels;
    private JPanel questionsContainer;
    private JScrollPane questionsScrollPane;
    
    // Action buttons
    private JButton addQuestionButton;
    private JButton createExamButton;
    
    public CreateExamPanel(Teacher teacher) {
        this.teacher = teacher;
        this.examDAO = new ExamDAO();
        this.questionDAO = new QuestionDAO();
        this.questionPanels = new ArrayList<>();
        
        // Set up the panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create components
        JPanel formPanel = createFormPanel();
        createQuestionsSection();
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createExamButton = new JButton("Create Exam");
        actionPanel.add(createExamButton);
        
        // Add components to panel
        add(formPanel, BorderLayout.NORTH);
        add(questionsScrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestionPanel();
            }
        });
        
        createExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createExam();
            }
        });
        
        // Add at least one question panel initially
        addQuestionPanel();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Create components
        JLabel titleLabel = new JLabel("Exam Title:");
        titleField = new JTextField(30);
        
        JLabel descLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(3, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        JLabel durationLabel = new JLabel("Duration (minutes):");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(30, 5, 180, 5);
        durationSpinner = new JSpinner(spinnerModel);
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(titleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(titleField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(descScrollPane, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(durationLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(durationSpinner, gbc);
        
        panel.setBorder(BorderFactory.createTitledBorder("Exam Details"));
        
        return panel;
    }
    
    private void createQuestionsSection() {
        // Create container for questions
        questionsContainer = new JPanel();
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        
        // Create scroll pane for questions
        questionsScrollPane = new JScrollPane(questionsContainer);
        questionsScrollPane.setBorder(BorderFactory.createTitledBorder("Exam Questions"));
        
        // Create button to add more questions
        addQuestionButton = new JButton("Add Question");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addQuestionButton);
        
        questionsContainer.add(buttonPanel);
    }
    
    private void addQuestionPanel() {
        QuestionPanel questionPanel = new QuestionPanel(questionPanels.size() + 1);
        questionPanels.add(questionPanel);
        
        // Add question panel before the Add Question button
        questionsContainer.remove(addQuestionButton.getParent());
        questionsContainer.add(questionPanel);
        
        // Re-add the button at the end
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addQuestionButton);
        questionsContainer.add(buttonPanel);
        
        // Update the UI
        questionsContainer.revalidate();
        questionsContainer.repaint();
    }
    
    private void createExam() {
        // Validate exam details
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        int duration = (Integer) durationSpinner.getValue();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an exam title",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (questionPanels.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add at least one question",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate all questions
        for (QuestionPanel qPanel : questionPanels) {
            if (!qPanel.validateFields()) {
                JOptionPane.showMessageDialog(this,
                        "Please complete all question fields",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        try {
            // Create the exam
            Exam exam = new Exam();
            exam.setTitle(title);
            exam.setDescription(description);
            exam.setDurationMinutes(duration);
            exam.setCreatedByTeacherId(teacher.getId());
            exam.setCreatedByTeacherName(teacher.getName());
            
            System.out.println("Creating exam: " + title + " by teacher: " + teacher.getName() + " (ID: " + teacher.getId() + ")");
            
            // Save exam to database
            int examId = examDAO.createExam(exam);
            System.out.println("Exam created with ID: " + examId);
            
            // Create and save all questions
            System.out.println("Adding " + questionPanels.size() + " questions to exam ID: " + examId);
            
            for (QuestionPanel qPanel : questionPanels) {
                Question question = qPanel.createQuestion(examId);
                System.out.println("Created question: " + question.getText() + " for exam ID: " + examId);
                int questionId = questionDAO.createQuestion(question);
                System.out.println("Question saved to database with ID: " + questionId);
            }
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                    "Exam created successfully with " + questionPanels.size() + " questions!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the form
            clearForm();
            
        } catch (SQLException e) {
            System.err.println("Error creating exam: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error creating exam: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        durationSpinner.setValue(30);
        
        // Remove all question panels
        questionPanels.clear();
        questionsContainer.removeAll();
        
        // Re-add the Add Question button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addQuestionButton);
        questionsContainer.add(buttonPanel);
        
        // Add one empty question panel
        addQuestionPanel();
        
        // Update the UI
        questionsContainer.revalidate();
        questionsContainer.repaint();
    }
    
    // Inner class for question panels
    private class QuestionPanel extends JPanel {
        private int questionNumber;
        private JTextArea questionTextArea;
        private JTextField option1Field, option2Field, option3Field, option4Field;
        private JRadioButton option1Radio, option2Radio, option3Radio, option4Radio;
        private ButtonGroup optionGroup;
        
        public QuestionPanel(int questionNumber) {
            this.questionNumber = questionNumber;
            
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Question " + questionNumber),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Question Text
            JLabel questionLabel = new JLabel("Question:");
            questionTextArea = new JTextArea(2, 40);
            questionTextArea.setLineWrap(true);
            questionTextArea.setWrapStyleWord(true);
            JScrollPane questionScrollPane = new JScrollPane(questionTextArea);
            
            // Options
            JLabel optionsLabel = new JLabel("Options (select the correct answer):");
            option1Field = new JTextField(30);
            option2Field = new JTextField(30);
            option3Field = new JTextField(30);
            option4Field = new JTextField(30);
            
            option1Radio = new JRadioButton("1.");
            option2Radio = new JRadioButton("2.");
            option3Radio = new JRadioButton("3.");
            option4Radio = new JRadioButton("4.");
            
            optionGroup = new ButtonGroup();
            optionGroup.add(option1Radio);
            optionGroup.add(option2Radio);
            optionGroup.add(option3Radio);
            optionGroup.add(option4Radio);
            
            // Option 1 row
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            add(questionLabel, gbc);
            
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            add(questionScrollPane, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4;
            add(optionsLabel, gbc);
            
            // Option 1 row
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            add(option1Radio, gbc);
            
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            add(option1Field, gbc);
            
            // Option 2 row
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            add(option2Radio, gbc);
            
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            add(option2Field, gbc);
            
            // Option 3 row
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            add(option3Radio, gbc);
            
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            add(option3Field, gbc);
            
            // Option 4 row
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            add(option4Radio, gbc);
            
            gbc.gridx = 1;
            gbc.gridwidth = 3;
            add(option4Field, gbc);
            
            // Remove button
            JButton removeButton = new JButton("Remove Question");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeQuestionPanel();
                }
            });
            
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 4;
            gbc.anchor = GridBagConstraints.EAST;
            add(removeButton, gbc);
            
            // Select first option by default
            option1Radio.setSelected(true);
        }
        
        public boolean validateFields() {
            if (questionTextArea.getText().trim().isEmpty()) return false;
            if (option1Field.getText().trim().isEmpty()) return false;
            if (option2Field.getText().trim().isEmpty()) return false;
            if (option3Field.getText().trim().isEmpty()) return false;
            if (option4Field.getText().trim().isEmpty()) return false;
            
            // Ensure one option is selected (though we default to option 1)
            return optionGroup.getSelection() != null;
        }
        
        public Question createQuestion(int examId) {
            Question question = new Question();
            question.setExamId(examId);
            question.setText(questionTextArea.getText().trim());
            question.setOption1(option1Field.getText().trim());
            question.setOption2(option2Field.getText().trim());
            question.setOption3(option3Field.getText().trim());
            question.setOption4(option4Field.getText().trim());
            
            // Determine correct option
            if (option1Radio.isSelected()) {
                question.setCorrectOption(1);
            } else if (option2Radio.isSelected()) {
                question.setCorrectOption(2);
            } else if (option3Radio.isSelected()) {
                question.setCorrectOption(3);
            } else if (option4Radio.isSelected()) {
                question.setCorrectOption(4);
            }
            
            System.out.println("Created question object: Text=" + question.getText() + 
                            ", Options=[" + question.getOption1() + ", " + 
                            question.getOption2() + ", " + 
                            question.getOption3() + ", " + 
                            question.getOption4() + "], " +
                            "Correct=" + question.getCorrectOption() + 
                            ", ExamID=" + question.getExamId());
            
            return question;
        }
        
        private void removeQuestionPanel() {
            // Don't allow removing if there's only one question
            if (questionPanels.size() <= 1) {
                JOptionPane.showMessageDialog(this,
                        "You must have at least one question",
                        "Cannot Remove",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Remove this panel
            questionPanels.remove(this);
            questionsContainer.remove(this);
            
            // Renumber remaining panels
            for (int i = 0; i < questionPanels.size(); i++) {
                questionPanels.get(i).updateQuestionNumber(i + 1);
            }
            
            // Update the UI
            questionsContainer.revalidate();
            questionsContainer.repaint();
        }
        
        public void updateQuestionNumber(int newNumber) {
            this.questionNumber = newNumber;
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Question " + questionNumber),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
    }
}
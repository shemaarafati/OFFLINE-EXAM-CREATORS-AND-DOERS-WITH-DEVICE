package com.offlineexam.view;

import com.offlineexam.model.Exam;
import com.offlineexam.model.Question;
import com.offlineexam.service.ExamService;

import javax.swing.*;
import java.awt.*;

public class QuestionEditorFrame extends JFrame {
    private Exam exam;
    public QuestionEditorFrame(Exam exam){ this.exam = exam; setTitle("Add Questions - Exam:"+exam.getId()); setSize(700,520); setLocationRelativeTo(null); init(); }
    private void init(){
        JPanel p = new JPanel(new GridLayout(10,1,6,6)); p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JTextField qtext = new JTextField(); JComboBox<String> type = new JComboBox<>(new String[]{"MCQ","Essay","TrueFalse"});
        p.add(new JLabel("Question Text:")); p.add(qtext); p.add(new JLabel("Type:")); p.add(type);
        JTextField optA = new JTextField(); JTextField optB = new JTextField(); JTextField optC = new JTextField(); JTextField optD = new JTextField();
        p.add(new JLabel("Option A:")); p.add(optA); p.add(new JLabel("Option B:")); p.add(optB);
        p.add(new JLabel("Option C (optional):")); p.add(optC); p.add(new JLabel("Option D (optional):")); p.add(optD);
        JTextField correctIndex = new JTextField(); p.add(new JLabel("Correct option index (0-based for MCQ):")); p.add(correctIndex);
        JButton add = new JButton("Add Question"); add.addActionListener(e -> {
            try{
                Question q = new Question(0, qtext.getText().trim(), (String)type.getSelectedItem());
                if(q.getType().equals("MCQ")){
                    q.addOption(optA.getText()); q.addOption(optB.getText()); if(!optC.getText().trim().isEmpty()) q.addOption(optC.getText()); if(!optD.getText().trim().isEmpty()) q.addOption(optD.getText());
                    q.setCorrectOptionIndex(Integer.parseInt(correctIndex.getText().trim()));
                } else if(q.getType().equals("TrueFalse")){
                    q.addOption("True"); q.addOption("False"); q.setCorrectOptionIndex(Integer.parseInt(correctIndex.getText().trim()));
                }
                exam.addQuestion(q); ExamService.saveAll(); JOptionPane.showMessageDialog(this, "Question added"); qtext.setText(""); optA.setText(""); optB.setText(""); optC.setText(""); optD.setText(""); correctIndex.setText(""); 
            } catch(Exception ex){ JOptionPane.showMessageDialog(this, "Failed to add question: " + ex.getMessage()); }
        });
        p.add(add);
        add(new JScrollPane(p));
    }
}

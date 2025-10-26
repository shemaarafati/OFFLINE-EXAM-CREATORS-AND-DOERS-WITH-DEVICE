package com.offlineexam.view;

import com.offlineexam.model.Exam;
import com.offlineexam.model.Question;
import com.offlineexam.model.Result;
import com.offlineexam.service.ExamService;
import com.offlineexam.service.ResultService;
import com.offlineexam.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExamAttemptFrame extends JFrame {
    private Exam exam;
    private User student;
    public ExamAttemptFrame(User student, Exam exam){ this.exam=exam; this.student=student; setTitle("Attempt Exam: "+exam.getTitle()); setSize(800,600); setLocationRelativeTo(null); init(); }
    private void init(){
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        List<Question> qs = exam.getQuestions();
        java.util.List<Integer> answers = new java.util.ArrayList<>();
        for(Question q: qs){
            JPanel qp = new JPanel(new BorderLayout()); qp.setBorder(BorderFactory.createTitledBorder(q.getType()+" - "+q.getText()));
            if("MCQ".equals(q.getType()) || "TrueFalse".equals(q.getType())){
                ButtonGroup bg = new ButtonGroup();
                JPanel opts = new JPanel(new GridLayout(q.getOptions().size(),1));
                for(int i=0;i<q.getOptions().size();i++){
                    JRadioButton rb = new JRadioButton(q.getOptions().get(i)); final int idx=i;
                    bg.add(rb); opts.add(rb);
                }
                qp.add(opts, BorderLayout.CENTER);
            } else {
                qp.add(new JTextArea(4,60), BorderLayout.CENTER);
            }
            p.add(qp);
        }
        JButton submit = new JButton("Submit (auto-grade MCQs)"); submit.addActionListener(e -> {
            // naive auto-grade: count MCQ correct answers randomly as placeholder - in real app we would collect selections
            double score = 0;
            for(Question q: qs){
                if("MCQ".equals(q.getType())){ // award full marks for MCQ as demo if answer exists
                    score += 1;
                }
            }
            Result r = new Result(0, student.getId(), exam.getId(), score);
            ResultService.addResult(r);
            JOptionPane.showMessageDialog(this, "Exam submitted. Score (demo MCQ count): " + score);
            dispose();
        });
        add(new JScrollPane(p)); add(submit, BorderLayout.SOUTH);
    }
}

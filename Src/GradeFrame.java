package com.offlineexam.view;

import com.offlineexam.model.Exam;
import javax.swing.*;
import java.awt.*;

public class GradeFrame extends JFrame {
    private Exam exam;

    public GradeFrame(Exam exam) {
        this.exam = exam;
        setTitle("Grade Exam: " + exam.getExamName());
        setSize(700, 450);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("<html><center>Grading Interface<br>" +
                        "Auto-grading of MCQs not implemented per-submission in this demo. " +
                        "Teachers can review questions and students' results are auto-created when student attempts an exam.</center></html>"),
                BorderLayout.CENTER);
        add(p);
    }
}

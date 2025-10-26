package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.model.Course;
import com.offlineexam.model.Exam;
import com.offlineexam.service.CourseService;
import com.offlineexam.service.ExamService;

import javax.swing.*;
import java.awt.*;

public class ExamCreatorFrame extends JFrame {
    private User teacher;
    public ExamCreatorFrame(User teacher){
        this.teacher = teacher;
        setTitle("Create Exam");
        setSize(700,480);
        setLocationRelativeTo(null);
        init();
    }

    private void init(){
        JPanel p = new JPanel(new GridLayout(6,2,6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("Course ID (must be assigned to you):"));
        JTextField courseId = new JTextField();
        p.add(courseId);
        p.add(new JLabel("Exam title:"));
        JTextField title = new JTextField();
        p.add(title);
        p.add(new JLabel("Duration (minutes):"));
        JTextField dur = new JTextField();
        p.add(dur);
        p.add(new JLabel(""));
        JButton create = new JButton("Create Exam");
        create.addActionListener(e -> {
            try{
                int cid = Integer.parseInt(courseId.getText().trim());
                Course c = CourseService.findById(cid); // Now calling static method
                if(c == null || c.getTeacher() == null || c.getTeacher().getId() != teacher.getId()){
                    JOptionPane.showMessageDialog(this, "You are not assigned to this course or course doesn't exist");
                    return;
                }
                Exam ex = new Exam(0, title.getText().trim(), cid, Integer.parseInt(dur.getText().trim()));
                ExamService.createExam(ex);
                JOptionPane.showMessageDialog(this, "Exam created: ID:"+ex.getId());
                new QuestionEditorFrame(ex).setVisible(true);
                dispose();
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });
        p.add(create);
        add(p);
    }
}
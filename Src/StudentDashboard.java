package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.model.Course;
import com.offlineexam.service.CourseService;
import com.offlineexam.service.RequestService;
import com.offlineexam.model.EnrollmentRequest;
import com.offlineexam.service.ExamService;
import com.offlineexam.model.Exam;
import com.offlineexam.service.ResultService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private User student;
    private DefaultListModel<String> courseModel = new DefaultListModel<>();

    public StudentDashboard(User student){
        this.student = student; setTitle("Student Dashboard - " + student.getUsername()); setSize(900,520); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE); init();
    }


    private void init(){
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Available Courses"));
        JList<String> list = new JList<>(courseModel);
        center.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh"); refresh.addActionListener(e -> loadCourses());
        JButton req = new JButton("Request Enroll"); req.addActionListener(e -> { int idx = list.getSelectedIndex(); if(idx>=0){ String s = courseModel.get(idx); int id = Integer.parseInt(s.split(" - ")[0]); EnrollmentRequest r = new EnrollmentRequest(0, student.getId(), id, "PENDING"); RequestService.sendRequest(r); JOptionPane.showMessageDialog(this, "Requested"); } });
        JButton exams = new JButton("View Exams"); exams.addActionListener(e -> { int idx = list.getSelectedIndex(); if(idx>=0){ String s = courseModel.get(idx); int id = Integer.parseInt(s.split(" - ")[0]); java.util.List<Exam> exs = ExamService.listByCourse(id); StringBuilder sb = new StringBuilder(); for(Exam ex: exs) sb.append(ex.getId()).append(" - ").append(ex.getTitle()).append("\n"); JOptionPane.showMessageDialog(this, sb.length()==0? new JLabel("No exams") : new JScrollPane(new JTextArea(sb.toString()))); } });
        JButton results = new JButton("My Results"); results.addActionListener(e -> { java.util.List<com.offlineexam.model.Result> rs = ResultService.listByStudent(student.getId()); StringBuilder sb = new StringBuilder(); for(com.offlineexam.model.Result r: rs) sb.append(r.toString()).append("\n"); JOptionPane.showMessageDialog(this, sb.length()==0? new JLabel("No results") : new JScrollPane(new JTextArea(sb.toString()))); });
        top.add(refresh); top.add(req); top.add(exams); top.add(results);

        add(top, BorderLayout.NORTH); add(center, BorderLayout.CENTER);
        loadCourses();
    }

    private void loadCourses(){ courseModel.clear(); java.util.List<Course> courses = CourseService.listAll(); for(Course c: courses) courseModel.addElement(c.getId()+" - "+c.getName()); }
}

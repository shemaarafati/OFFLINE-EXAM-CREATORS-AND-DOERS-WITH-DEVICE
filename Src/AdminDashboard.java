package com.offlineexam.view;

import com.offlineexam.model.Course;
import com.offlineexam.model.User;
import com.offlineexam.service.AuthService;
import com.offlineexam.service.CourseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private DefaultListModel<String> teacherModel = new DefaultListModel<>();
    private DefaultListModel<String> courseModel = new DefaultListModel<>();

    public AdminDashboard(){
        setTitle("Admin Dashboard");
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }

    private void init(){
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createTitledBorder("Teachers"));
        JList<String> teacherList = new JList<>(teacherModel);
        left.add(new JScrollPane(teacherList), BorderLayout.CENTER);
        JButton refreshT = new JButton("Refresh Teachers");
        refreshT.addActionListener(e -> loadTeachers());
        left.add(refreshT, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Courses"));
        JList<String> courseList = new JList<>(courseModel);
        right.add(new JScrollPane(courseList), BorderLayout.CENTER);
        JButton refreshC = new JButton("Refresh Courses");
        refreshC.addActionListener(e -> loadCourses());
        right.add(refreshC, BorderLayout.SOUTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addTeacher = new JButton("Add Teacher");
        addTeacher.addActionListener(e -> addTeacher());
        JButton addCourse = new JButton("Create Course");
        addCourse.addActionListener(e -> createCourse());
        JButton assign = new JButton("Assign Teacher");
        assign.addActionListener(e -> assignTeacher());
        top.add(addTeacher);
        top.add(addCourse);
        top.add(assign);

        add(top, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
        loadTeachers();
        loadCourses();
    }

    private void loadTeachers(){
        teacherModel.clear();
        List<User> list = AuthService.listByRole("teacher"); // Now static
        for(User u: list)
            teacherModel.addElement(u.getId()+": "+u.getUsername()+" - "+(u.getFullName()!=null?u.getFullName():""));
    }

    private void loadCourses(){
        courseModel.clear();
        List<Course> list = CourseService.listAll(); // Now static
        for(Course c: list)
            courseModel.addElement(c.toString());
    }

    private void addTeacher(){
        JTextField username = new JTextField();
        JTextField fullname = new JTextField();
        JPasswordField pass = new JPasswordField();
        Object[] fields = {"Username:", username, "Full name:", fullname, "Password:", pass};
        int res = JOptionPane.showConfirmDialog(this, fields, "Add Teacher", JOptionPane.OK_CANCEL_OPTION);
        if(res == JOptionPane.OK_OPTION){
            User u = new User(0, fullname.getText().trim(), username.getText().trim(),
                    new String(pass.getPassword()), "teacher", null);
            boolean ok = AuthService.register(u); // Now static
            if(ok)
                JOptionPane.showMessageDialog(this, "Teacher created");
            else
                JOptionPane.showMessageDialog(this, "Failed (username exists)");
            loadTeachers();
        }
    }

    private void createCourse(){
        JTextField name = new JTextField();
        JTextField desc = new JTextField();
        Object[] fields = {"Course name:", name, "Description:", desc};
        if(JOptionPane.showConfirmDialog(this, fields, "Create Course", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            Course c = new Course(0, name.getText().trim(), desc.getText().trim());
            CourseService.createCourse(c); // Now static
            JOptionPane.showMessageDialog(this, "Course created");
            loadCourses();
        }
    }

    private void assignTeacher(){
        String cid = JOptionPane.showInputDialog(this, "Course ID to assign:");
        String tid = JOptionPane.showInputDialog(this, "Teacher ID:");
        try{
            int c = Integer.parseInt(cid);
            int t = Integer.parseInt(tid);
            CourseService.assignTeacher(c,t); // Now static
            JOptionPane.showMessageDialog(this, "Assigned");
            loadCourses();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Invalid IDs");
        }
    }
}

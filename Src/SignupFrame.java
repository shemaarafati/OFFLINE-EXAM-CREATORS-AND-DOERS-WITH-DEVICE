package com.offlineexam.view;

import com.offlineexam.model.User;
import com.offlineexam.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {
    private JTextField userField, nameField, emailField;
    private JPasswordField passField;
    private JComboBox<String> roleCombo;

    public SignupFrame(){
        setTitle("Sign up");
        setSize(420,300);
        setLocationRelativeTo(null);
        init();
    }

    private void init(){
        JPanel p = new JPanel(new GridLayout(6,2,6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("Role:")); roleCombo = new JComboBox<>(new String[]{"student","teacher"}); p.add(roleCombo);
        p.add(new JLabel("Full name:")); nameField = new JTextField(); p.add(nameField);
        p.add(new JLabel("Email:")); emailField = new JTextField(); p.add(emailField);
        p.add(new JLabel("Username:")); userField = new JTextField(); p.add(userField);
        p.add(new JLabel("Password:")); passField = new JPasswordField(); p.add(passField);
        p.add(new JLabel()); JButton create = new JButton("Create"); create.addActionListener(e -> doCreate()); p.add(create);
        add(p);
    }

    private void doCreate(){
        String role = (String) roleCombo.getSelectedItem();
        User u = new User(0, nameField.getText().trim(), userField.getText().trim(),
                new String(passField.getPassword()), role, emailField.getText().trim());
        boolean ok = AuthService.register(u); // Now calling static method
        if(ok){
            JOptionPane.showMessageDialog(this, "Account created - login now");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists");
        }
    }
}
package com.offlineexam.service;

import com.offlineexam.model.User;
import com.offlineexam.util.DataStore;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static final String USER_FILE = "users.dat";
    private static List<User> users = DataStore.load(USER_FILE);
    private static int idSeq = 1;

    static {
        for (User u : users) {
            if (u.getId() >= idSeq) idSeq = u.getId() + 1;
        }
        // Create default admin if no users exist
        if (users.isEmpty()) {
            User admin = new User(idSeq++, "System Admin", "admin", "admin", "admin", "admin@system.com");
            users.add(admin);
            DataStore.save(USER_FILE, users);
        }
    }

    public static User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }

    public static boolean register(User user) {
        // Check if username already exists
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        user.setId(idSeq++);
        users.add(user);
        DataStore.save(USER_FILE, users);
        return true;
    }

    public static List<User> listByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getRole().equalsIgnoreCase(role)) result.add(u);
        }
        return result;
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
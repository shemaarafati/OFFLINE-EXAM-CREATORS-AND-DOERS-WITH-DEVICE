package com.offlineexam.service;

import com.offlineexam.model.User;
import com.offlineexam.util.DataStore;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String USER_FILE = "users.dat";
    private static List<User> users = DataStore.load(USER_FILE);

    public static User findById(int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    public static User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
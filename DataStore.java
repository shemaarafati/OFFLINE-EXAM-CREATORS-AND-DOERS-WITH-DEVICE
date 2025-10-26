package com.offlineexam.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    // Load list of objects from file
    @SuppressWarnings("unchecked")
    public static <T> List<T> load(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save list of objects to file
    public static <T> void save(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

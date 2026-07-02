package com.fpt.lms.service;

import com.fpt.lms.model.LmsUser;
import com.fpt.lms.model.Student;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LmsDataService {

    private static final String USERS_FILE = "users.dat";
    private static final String STUDENTS_FILE = "students.dat";

    public void saveUsers(List<LmsUser> users) {
        saveList(USERS_FILE, users);
    }

    public List<LmsUser> getUsers() {
        return loadList(USERS_FILE);
    }

    public void saveStudents(List<Student> students) {
        saveList(STUDENTS_FILE, students);
    }

    public List<Student> getStudents() {
        return loadList(STUDENTS_FILE);
    }

    private <T extends Serializable> void saveList(String filename, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
            System.out.println("Saved data to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> List<T> loadList(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}


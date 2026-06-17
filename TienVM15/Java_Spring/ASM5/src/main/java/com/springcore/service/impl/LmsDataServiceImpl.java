package com.springcore.service.impl;

import com.springcore.model.LmsUser;
import com.springcore.model.Student;
import com.springcore.service.LmsDataService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LmsDataServiceImpl implements LmsDataService {

    private static final String USERS_FILE = "users.dat";
    private static final String STUDENTS_FILE = "students.dat";

    @Override
    public void saveUsers(List<LmsUser> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Successfully saved " + users.size() + " users to " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LmsUser> getUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("Users file " + USERS_FILE + " does not exist yet.");
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<LmsUser>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
            System.out.println("Successfully saved " + students.size() + " students to " + STUDENTS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Student> getStudents() {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) {
            System.out.println("Students file " + STUDENTS_FILE + " does not exist yet.");
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENTS_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Student>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

package com.example.asm3.service;

import com.example.asm3.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final String FILE_NAME = "courses.dat";

    // Function to save the list of courses to courses.dat using ObjectOutputStream
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Courses have been successfully saved to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving courses to file: " + e.getMessage());
        }
    }

    // Function to read the list of courses from courses.dat using ObjectInputStream
    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println(FILE_NAME + " not found. Returning empty list.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading courses from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

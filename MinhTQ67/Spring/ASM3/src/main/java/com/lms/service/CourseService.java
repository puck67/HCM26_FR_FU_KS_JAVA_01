package com.lms.service;

import com.lms.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final String FILE_NAME = "courses.dat";

    /**
     * Save list of courses to courses.dat file using ObjectOutputStream
     */
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println(">>> Saved " + courses.size() + " course(s) to file: " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read courses from courses.dat file and return as List<Course> using ObjectInputStream
     */
    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.err.println("File not found: " + FILE_NAME);
            return courses;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            courses = (List<Course>) ois.readObject();
            System.out.println(">>> Read " + courses.size() + " course(s) from file: " + FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return courses;
    }
}

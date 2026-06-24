package com.fpt.lms.service;

import com.fpt.lms.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class CourseService {

    private static final String FILE_PATH = "courses.dat";

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(courses);
            System.out.println("Saved " + courses.size() + " courses to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return List.of();
        }
    }
}

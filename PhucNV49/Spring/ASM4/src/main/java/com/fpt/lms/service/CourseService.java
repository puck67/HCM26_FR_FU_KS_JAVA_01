package com.fpt.lms.service;

import com.fpt.lms.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private static final String FILE_NAME = "courses.dat";

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Courses saved to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

package com.lms.service;

import com.lms.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("courses.dat"))) {
            oos.writeObject(courses);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File("courses.dat");
        if (!file.exists()) {
            return courses;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            courses = (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading courses: " + e.getMessage());
        }
        return courses;
    }
}

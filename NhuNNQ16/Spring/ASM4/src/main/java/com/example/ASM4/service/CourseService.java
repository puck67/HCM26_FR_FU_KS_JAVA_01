package com.example.ASM4.service;

import com.example.ASM4.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private static final String FILE_NAME = "courses.dat";

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Saved " + courses.size() + " courses to " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return courses;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            courses = (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return courses;
    }
}

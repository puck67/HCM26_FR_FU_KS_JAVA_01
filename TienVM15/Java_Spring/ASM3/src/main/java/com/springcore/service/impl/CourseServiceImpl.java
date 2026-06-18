package com.springcore.service.impl;

import com.springcore.model.Course;
import com.springcore.service.CourseService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final String FILE_NAME = "courses.dat";

    @Override
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Successfully saved " + courses.size() + " courses to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Data file " + FILE_NAME + " does not exist yet.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Course>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
